package com.chat.config

import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import reactor.core.publisher.Flux
import reactor.kafka.sender.KafkaSender
import reactor.kafka.sender.SenderOptions
import reactor.kafka.sender.SenderRecord
import java.time.Duration
import java.util.*

fun main() {
    val properties = Properties()
    properties[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = "http://localhost:39092"
    properties[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
    properties[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java

    val flux = Flux.range(1, 1000000)
        .delayElements(Duration.ofSeconds(1))
        .map { ProducerRecord("order-events", null,"${it % 13}", "!!! $it") }
        .map { SenderRecord.create(it, it.key()) }

    val start = System.currentTimeMillis()
    KafkaSender.create(
        SenderOptions.create<String, String>(properties)
            .maxInFlight(10000)
    )
        .send(flux)
        .doOnNext { message ->
            println("Message from user service. id: ${message.correlationMetadata()}, value: ${message}")
        }
        .doOnComplete {
            println("Total time: ${System.currentTimeMillis() - start}")
        }
        .subscribe()
}