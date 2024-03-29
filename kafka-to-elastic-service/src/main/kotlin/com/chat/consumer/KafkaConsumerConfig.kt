package com.chat.consumer

import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.CooperativeStickyAssignor
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate
import reactor.kafka.receiver.KafkaReceiver
import reactor.kafka.receiver.ReceiverOptions
import reactor.util.retry.Retry
import java.time.Duration
import java.util.*
import kotlin.reflect.jvm.jvmName

@Configuration
class KafkaConsumerConfig {

    @Value("\${kafka-to-elastic-service.consumer.topic.user}")
    private lateinit var userTopic: String

    @Bean("user-to-elastic-option")
    fun receiverOps(kafkaProperties: KafkaProperties): ReceiverOptions<String, String> {
        return ReceiverOptions.create<String?, String?>(kafkaProperties.buildConsumerProperties())
            .consumerProperty(ConsumerConfig.GROUP_INSTANCE_ID_CONFIG, UUID.randomUUID().toString())
            .consumerProperty(ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG, "false")
            .consumerProperty(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, "true")
            .consumerProperty(ConsumerConfig.GROUP_ID_CONFIG, "KAFKA_TO_ELASTIC_CONSUMER_GROUP")
            .consumerProperty(
                ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG,
                CooperativeStickyAssignor::class.jvmName
            )
            .consumerProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 500)
            .commitInterval(Duration.ofSeconds(1)).subscription(listOf(userTopic))
    }

    @Bean("user-to-elastic")
    fun reactiveKafkaConsumerTemplate(
        @Qualifier("user-to-elastic-option") receiverOptions: ReceiverOptions<String, String>
    ):
            ReactiveKafkaConsumerTemplate<String, String> {
        return ReactiveKafkaConsumerTemplate(receiverOptions)
    }

    @Bean("user-to-elastic-receiver")
    fun reactiveUserElastic(
        @Qualifier("user-to-elastic-option") receiverOptions: ReceiverOptions<String, String>
    ):
            KafkaReceiver<String, String> {
        return KafkaReceiver.create(receiverOptions)
    }

    @Bean("kafka-to-elastic-retry")
    fun retryConfig(): Retry {
        return Retry
            .fixedDelay(3, Duration.ofSeconds(2))
            .filter { IndexOutOfBoundsException::class.java.isInstance(it) }
            .onRetryExhaustedThrow { spec, signal -> signal.failure() }
    }
}