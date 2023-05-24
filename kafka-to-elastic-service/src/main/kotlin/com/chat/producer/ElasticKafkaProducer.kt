package com.chat.producer

import com.chat.kafka.avro.model.UserAvroModel
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import reactor.kafka.sender.SenderOptions
import java.util.*

//@Configuration
//class ElasticKafkaProducer {
//
//    @Bean("user-to-elastic-producer-option")
//    fun receiverOps(kafkaProperties: KafkaProperties): SenderOptions<String, UserAvroModel> {
//        return SenderOptions.create(kafkaProperties.buildProducerProperties())
//    }
//
//    @Bean("user-to-elastic-producer-template")
//    fun reactiveKafkaProducerTemplate(
//        @Qualifier("user-to-elastic-producer-option")
//        receiverOptions: SenderOptions<String, UserAvroModel>
//    ): ReactiveKafkaProducerTemplate<String, UserAvroModel> {
//        return ReactiveKafkaProducerTemplate(receiverOptions)
//    }
//}