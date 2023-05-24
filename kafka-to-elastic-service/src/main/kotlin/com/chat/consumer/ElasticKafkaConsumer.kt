package com.chat.consumer

import com.chat.config.LoggingConfig
import com.chat.kafka.avro.model.UserAvroModel
import com.chat.model.UserElasticEntity
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate
import org.springframework.stereotype.Component
import java.util.*
import javax.annotation.PostConstruct

@Component
class ElasticKafkaConsumer(@Qualifier("user-to-elastic") private val kafkaConsumerTemplate: ReactiveKafkaConsumerTemplate<String, UserAvroModel>,
                           private val LOG: LoggingConfig,
                           private val reactiveElasticsearchTemplate: ReactiveElasticsearchOperations) {

    @PostConstruct
    fun receiveUserModel() {
        this.kafkaConsumerTemplate
                .receive()
                .doOnNext { message ->
                    LOG.kafkaToElastic.info("Message from user service. key: {}, user: {}", message.key(), message.value())
                }
                .doOnNext {
                    this.reactiveElasticsearchTemplate.save(UserElasticEntity
                            .builder()
                            .username(it.value().username.toString())
                            .id(it.value().id)
                            .name(it.value().name.toString())
                            .createdDate(Date(it.value().createdDate))
                            .build()
                    )
                }
                .doOnNext {
                    it.receiverOffset().acknowledge()
                }.subscribe()
    }
}