package com.chat.kafka.sec15

import com.chat.sec15.DelayType
import org.apache.kafka.clients.producer.ProducerRecord
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kafka.sender.KafkaSender
import reactor.kafka.sender.SenderRecord
import reactor.kafka.sender.SenderResult
import reactor.kotlin.core.publisher.switchIfEmpty
import java.time.Duration
import java.util.function.Predicate

class TransferEventProcessor(private val sender: KafkaSender<String, String>) {

    fun process(flux: Flux<TransferEvent>): Flux<SenderResult<String>> {
        return flux
            .concatMap { validate(it) }
            .concatMap { this.sendTransaction(it) }
    }

    private fun sendTransaction(event: TransferEvent): Flux<SenderResult<String>> {
        val senderRecords = this.senderRecords(event)
        val manager = this.sender.transactionManager()
        return manager.begin<Mono<TransferEvent>>()
//            .retryExponentialBackoff(
//                3,
//                Duration.ofSeconds(1),
//                Duration.ofSeconds(10),
//                true,
//                doOnRetry = { retryContext ->
//
//                })
            .thenMany(
                this.sender
                    .send(senderRecords)
                    .timeout(Duration.ofSeconds(10))
                    .concatWith(
                        Mono.delay(Duration.ofSeconds(calculateDelay(1, DelayType.NORMAL)))
                            .then(Mono.fromRunnable(event.ack))
                    )
                    .concatWith(manager.commit())
            )
            .doOnError { println("Sending records failed with error message: ${it.localizedMessage}") }
            .onErrorResume { manager.abort() }
    }

    private fun calculateDelay(delay: Long, type: DelayType): Long {
        return delay
    }

    private fun validate(event: TransferEvent): Mono<TransferEvent> {
        return Mono.just(event)
            .filter(Predicate.not { e -> e.key == "5" })
            .switchIfEmpty {
                Mono.fromRunnable<TransferEvent?> {
                    event.ack
                }.doFirst { println("Validation failed ${event.key}") }
            }
    }

    private fun senderRecords(event: TransferEvent): Flux<SenderRecord<String, String, String>> {
        val p1 = ProducerRecord("transaction-events", event.key, "%s+%s".format(event.to, event.amount))
        val p2 = ProducerRecord("transaction-events", event.key, "%s-%s".format(event.from, event.amount))

        val s1 = SenderRecord.create(p1, p1.key())
        val s2 = SenderRecord.create(p2, p2.key())

        return Flux.just(s1, s2)
    }
}