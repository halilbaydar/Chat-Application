package com.chat.consumer

import com.chat.kafka.avro.model.UserAvroModel
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate
import reactor.kafka.receiver.ReceiverOptions
import java.util.*

@Configuration
class KafkaConsumerConfig {

    @Value("\${kafka-to-elastic-service.consumer.topic.user}")
    private lateinit var userTopic: String

    @Bean("user-to-elastic-option")
    fun receiverOps(kafkaProperties: KafkaProperties): ReceiverOptions<String, UserAvroModel> {
        return ReceiverOptions.create<String?, UserAvroModel?>(kafkaProperties.buildConsumerProperties())
            .consumerProperty(ConsumerConfig.GROUP_INSTANCE_ID_CONFIG, UUID.randomUUID().toString())
            .consumerProperty(ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG, "false")
            .subscription(listOf(userTopic))
    }

    @Bean("user-to-elastic")
    fun reactiveKafkaConsumerTemplate(
        @Qualifier("user-to-elastic-option")
        receiverOptions: ReceiverOptions<String, UserAvroModel>
    ): ReactiveKafkaConsumerTemplate<String, UserAvroModel> {
        return ReactiveKafkaConsumerTemplate(receiverOptions)
    }
}