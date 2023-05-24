package com.chat.kafka.sec15

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.CooperativeStickyAssignor
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import reactor.kafka.receiver.KafkaReceiver
import reactor.kafka.receiver.ReceiverOptions
import reactor.kafka.sender.KafkaSender
import reactor.kafka.sender.SenderOptions
import java.time.Duration
import java.util.*
import kotlin.reflect.jvm.jvmName

class TransferDemo {

    companion object {
        fun kafkaReceiver(instanceId: String? = null): KafkaReceiver<String, String> {
            val properties = Properties()
            properties[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = "http://localhost:19092"
            properties[ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG] = true
            properties[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "earliest"
            properties[ConsumerConfig.GROUP_ID_CONFIG] = "transaction-demo"
            properties[ConsumerConfig.GROUP_INSTANCE_ID_CONFIG] = instanceId
            properties[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
            properties[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
            properties[ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG] =
                CooperativeStickyAssignor::class.jvmName
            // properties[ConsumerConfig.MAX_POLL_RECORDS_CONFIG] = 4

            return KafkaReceiver.create(
                ReceiverOptions.create<String, String>(properties)
                    .commitInterval(Duration.ofSeconds(1))
                    .subscription(listOf("transfer-requests"))
            )
        }

        fun kafkaSender(): KafkaSender<String, String> {
            val properties = Properties()
            properties[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = "http://localhost:39092"
            properties[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
            properties[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
            properties[ProducerConfig.TRANSACTIONAL_ID_CONFIG] = UUID.randomUUID().toString()
            return KafkaSender.create(SenderOptions.create(properties))
        }
    }
}

fun main() {
    val transferEventConsumer = TransferEventConsumer(TransferDemo.kafkaReceiver("transaction-instance-1"))
    val transferEventProcessor = TransferEventProcessor(TransferDemo.kafkaSender())

    transferEventConsumer
        .receive()
        .timeout(Duration.ofSeconds(10))
        .transform { transferEventProcessor.process(it) }
        .doOnNext { println("Transfer success: ${it.correlationMetadata()}") }
        .doOnError { ex -> println("Error ${ex.message}") }
        .subscribe()
}