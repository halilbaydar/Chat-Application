package com.chat.consumer

import com.chat.config.LoggingConfig
import com.chat.kafka.avro.model.UserAvroModel
import com.chat.service.ElasticClientService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate
import org.springframework.stereotype.Component
import java.util.*
import javax.annotation.PostConstruct

@Component
class ElasticKafkaConsumer(@Qualifier("user-to-elastic") private val kafkaConsumerTemplate: ReactiveKafkaConsumerTemplate<String, UserAvroModel>,
                           private val LOG: LoggingConfig,
                           private val elasticClientService: ElasticClientService) {

    @PostConstruct
    fun receiveUserModel() {
        this.kafkaConsumerTemplate
                .receive()
                .doOnNext { message ->
                    LOG.kafkaToElastic.info("Message from user service. key: {}, user: {}", message.key(), message.value())
                }
                .doOnNext {
                    it.receiverOffset().acknowledge()
                }
                .flatMap {
                    this.elasticClientService.save(it.value())
                }
                .doOnError {
                    LOG.kafkaToElastic.error("Error while saving user to elastic with message: ${it.message}")
                }
                .subscribe()
    }
}