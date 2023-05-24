package com.chat.config

//import io.confluent.kafka.serializers.KafkaAvroDeserializer
import lombok.extern.slf4j.Slf4j
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.CooperativeStickyAssignor
import org.apache.kafka.common.serialization.LongDeserializer
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.publisher.Flux
import reactor.core.publisher.GroupedFlux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import reactor.kafka.receiver.KafkaReceiver
import reactor.kafka.receiver.ReceiverOptions
import reactor.kafka.receiver.ReceiverRecord
import reactor.util.retry.Retry
import java.time.Duration
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.reflect.jvm.jvmName

@Slf4j
//@Configuration
class ElasticKafkaReceiver {

//    @Bean
    fun kafkaReceiver(): KafkaReceiver<Any, Any> {
        val properties = Properties()
        properties[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = "http://localhost:19092"
        properties[ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG] = true
        properties[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "earliest"
        properties[ConsumerConfig.GROUP_ID_CONFIG] = "kafka-to-elastic-service"
        properties[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = LongDeserializer::class.java
//        properties[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = KafkaAvroDeserializer::class.java
        return KafkaReceiver.create(ReceiverOptions.create(properties))
    }

    companion object {
        fun run(instanceId: String) {
            val properties = Properties()
            properties[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = "http://localhost:19092"
            properties[ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG] = true
            properties[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "earliest"
            properties[ConsumerConfig.GROUP_ID_CONFIG] = "demo-group"
            properties[ConsumerConfig.GROUP_INSTANCE_ID_CONFIG] = instanceId
            properties[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
            properties[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
            properties[ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG] =
                CooperativeStickyAssignor::class.jvmName
            // properties[ConsumerConfig.MAX_POLL_RECORDS_CONFIG] = 4

            KafkaReceiver.create(
                ReceiverOptions.create<String, String>(properties)
                    .commitInterval(Duration.ofSeconds(1))
                    .subscription(listOf("order-events"))
            )

                //SEQUENTIAL BATCH PROCESSING
                //.receiveAutoAck()
                //.concatMap { batchProcess(it) }

                ///PARALLEL BATCH PROCESSING WITH RECORD ORDERING
//                .receive()
//                 .groupBy { record ->
//                     Integer.parseInt(record.key()) % 5
//                }
//                .flatMap({ batchProcess(it) }, 10)

                ///PARALLEL BATCH PROCESSING WITHOUT RECORD ORDERING
                //.receiveAutoAck()
                //.flatMap({ batchProcess(it) }, 10)

                ///SEQUENTIAL BATCH PROCESSING WITHOUT RECORD ORDERING AND PROCESSING IN DIFFERENT STREAM
                .receive()
                .concatMap { process(it) }
                .subscribe()
        }

        private fun batchProcess(flux: GroupedFlux<Int, ReceiverRecord<String, String>>): Mono<Void> {
            return flux
                .publishOn(Schedulers.boundedElastic())
                .doFirst {
                    println("Flux process starting...., mod: ${flux.key()}")
                }
                .log()
                .doOnNext { message ->
                    println("Message to user service. key: ${message.key()}, user: ${message.value()}")
                }
                .doOnNext { record -> record.receiverOffset().acknowledge() }
                .then(Mono.delay(Duration.ofSeconds(1)))
                .then()
        }

        private fun process(receiverRecord: ReceiverRecord<String, String>): Mono<Void> {
            return Mono.just(receiverRecord)
                .doOnNext { message ->
                    if (message.key().toString() == "3") {
                        throw RuntimeException("@@@@@@@@@@ AN ERROR OCCURRED key: ${message.key()} @@@@@@@@@@@@@@")
                    }
                    println("logginggg ........ -------------------->>>>>>>>>>")
                    val index = ThreadLocalRandom.current().nextInt(10, 12)
                    println("index::::: $index")
                    println(
                        "Message to user service. key: ${message.key()}, value: ${message.value()}, index: $index, ${
                            message.value().toCharArray()[index]
                        }"
                    )
                    message.receiverOffset().acknowledge()
                }
                .retryWhen(retryConfig())
                .onErrorResume(IndexOutOfBoundsException::class.java) {
                    Mono.fromRunnable { receiverRecord.receiverOffset().acknowledge() }
                }
                .then()
        }

        private fun batchProcess(flux: Flux<ConsumerRecord<String, String>>): Mono<Void> {
            return flux
                .publishOn(Schedulers.boundedElastic())
                .doFirst {
                    println("Flux process starting....")
                }
                .log()
                .doOnNext { message ->
                    println("Message to user service. key: ${message.key()}, user: ${message.value()}")
                }
                .then(Mono.delay(Duration.ofSeconds(1)))
                .then()
        }

        private fun retryConfig(): Retry {
            return Retry.fixedDelay(3, Duration.ofSeconds(2))
                .filter { IndexOutOfBoundsException::class.java.isInstance(it) }
                .onRetryExhaustedThrow { spec, signal -> signal.failure() }
        }
    }
}

