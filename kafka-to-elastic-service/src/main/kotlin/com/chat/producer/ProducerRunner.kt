package com.chat.producer

import com.chat.kafka.avro.model.UserAvroModel
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.util.*

//@Service
//class ProducerRunner(
//    @Qualifier("user-to-elastic-producer-template")
//    private val reactiveKafkaProducerTemplate: ReactiveKafkaProducerTemplate<String, UserAvroModel>
//) : CommandLineRunner {
//
//    @Value("\${kafka-to-elastic-service.consumer.topic.user}")
//    private lateinit var userTopic: String
//
//    override fun run(vararg args: String?) {
//        Flux.range(0, 1)
//            .map {
//                UserAvroModel
//                    .newBuilder()
//                    .setId(UUID.randomUUID().toString())
//                    .setName("random-name")
//                    .setUsername("username-1")
//                    .setCreatedDate(Date().time)
//                    .build()
//            }
//            .doOnNext { println("Data: $it") }
//            .flatMap { this.reactiveKafkaProducerTemplate.send(userTopic, it.id, it) }
//            .doOnError { println("Error: ${it.message}") }
//            .doOnNext { println("Result:  ${it.recordMetadata()}") }
//            .subscribe()
//    }
//}