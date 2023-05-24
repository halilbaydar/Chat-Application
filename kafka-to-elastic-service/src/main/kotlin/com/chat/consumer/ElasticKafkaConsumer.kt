package com.chat.consumer

import com.chat.config.LoggingConfig
import com.chat.kafka.avro.model.UserAvroModel
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class ElasticKafkaConsumer(@Qualifier("user-to-elastic") private val kafkaConsumerTemplate: ReactiveKafkaConsumerTemplate<String, UserAvroModel>,
                           private val LOG: LoggingConfig) {

    @PostConstruct
    fun receiver() {
        this.kafkaConsumerTemplate.receive().doOnNext { message ->
            LOG.kafkaToElastic.info("Message from user service. key: {}, user: {}", message.key(), message.value())
        }.doOnNext { it.receiverOffset().acknowledge() }.subscribe()
    }
}