package com.chat

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.CooperativeStickyAssignor
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.junit.jupiter.api.Test
import org.springframework.kafka.test.context.EmbeddedKafka
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kafka.receiver.KafkaReceiver
import reactor.kafka.receiver.ReceiverOptions
import reactor.kafka.sender.KafkaSender
import reactor.kafka.sender.SenderOptions
import reactor.kafka.sender.SenderRecord
import reactor.test.StepVerifier
import java.time.Duration
import java.util.*
import kotlin.reflect.jvm.jvmName

@EmbeddedKafka(
    ports = [9092],
    partitions = 1,
    topics = ["user-to-elastic"],
    brokerProperties = ["auto.create.topics.enable=false"]
)
class KafkaTest {

    @Test
    fun test() {
        StepVerifier.create(Producer.run()).verifyComplete()
        StepVerifier.create(Consumer.run()).verifyComplete()
    }

    companion object {
        class Consumer {
            companion object {
                fun run(): Mono<Void> {
                    val properties = Properties()
                    properties[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = "http://localhost:9092"
                    properties[ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG] = true
                    properties[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "earliest"
                    properties[ConsumerConfig.GROUP_ID_CONFIG] = "demo-group"
                    properties[ConsumerConfig.GROUP_INSTANCE_ID_CONFIG] = "instanceId"
                    properties[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
                    properties[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
                    properties[ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG] =
                        CooperativeStickyAssignor::class.jvmName
                    // properties[ConsumerConfig.MAX_POLL_RECORDS_CONFIG] = 4

                    return KafkaReceiver.create(
                        ReceiverOptions.create<String, String>(properties)
                            .commitInterval(Duration.ofSeconds(1))
                            .subscription(listOf("user-to-elastic"))
                    )
                        .receive()
                        .take(10)
                        .doOnNext { println("Message received key: ${it.key()}, value: ${it.value()}") }
                        .doOnNext { it.receiverOffset().acknowledge() }
                        .then()
                }
            }
        }

        class Producer {
            companion object {
                fun run(): Mono<Void> {
                    val properties = Properties()
                    properties[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = "http://localhost:9092"
                    properties[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
                    properties[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java

                    val flux = Flux.range(1, 10)
                        .delayElements(Duration.ofMillis(10))
                        .map { ProducerRecord("user-to-elastic", null, "${it % 13}", "!!! $it") }
                        .map { SenderRecord.create(it, it.key()) }

                    val start = System.currentTimeMillis()

                    return KafkaSender.create(SenderOptions.create<String, String>(properties))
                        .send(flux)
                        .doOnNext { message ->
                            println("Message from user service. id: ${message.correlationMetadata()}, value: ${message}")
                        }
                        .doOnComplete {
                            println("Total time: ${System.currentTimeMillis() - start}")
                        }.then()
                }
            }
        }
    }
}