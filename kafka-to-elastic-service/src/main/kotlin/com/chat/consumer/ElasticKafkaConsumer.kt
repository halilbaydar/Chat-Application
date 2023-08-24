package com.chat.consumer

import com.chat.config.LoggingConfig
import com.chat.exception.ElasticSaveException
import com.chat.model.KafkaUserModel
import com.chat.model.UserModel
import com.chat.service.ElasticClientService
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.CommandLineRunner
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import reactor.kafka.receiver.KafkaReceiver
import reactor.util.retry.Retry
import java.util.*

@Component
class ElasticKafkaConsumer(
    @Qualifier("user-to-elastic")
    private val kafkaConsumerTemplate: ReactiveKafkaConsumerTemplate<String, String>,
    private val userToElasticReceiver: KafkaReceiver<String, String>,
    private val LOG: LoggingConfig,
    private val elasticClientService: ElasticClientService,
    @Qualifier("kafka-to-elastic-retry")
    private val retry: Retry,
    private val objectMapper: ObjectMapper
) : CommandLineRunner {

    fun receiveUserModel() {
        this.userToElasticReceiver
            .receiveAutoAck()
            .flatMap({ batchProcess(it) }, 10)
            .retryWhen(retry)
            .doOnError {
                LOG.kafkaToElastic.error("Error while saving user to elastic with message: ${it.message}")
            }
            .subscribe()
    }

    private fun batchProcess(flux: Flux<ConsumerRecord<String, String>>): Mono<Void> {
        return flux
            .publishOn(Schedulers.newBoundedElastic(3, 10, "user-to-elastic-batch-consumer"))
            .doOnNext { message ->
                println("Message to user service. key: ${message.key()}, value: ${message.value()}")
            }
            .map { objectMapper.readValue(it.value(), UserModel::class.java) }
            .collectList()
            .flatMapMany { this.elasticClientService.saveAll(it) }
            .doOnError {
                println("Error while saving: ${it.message}")
            }.doOnError { throw ElasticSaveException(it.message!!, it.cause) }
            .then()
    }

    override fun run(vararg args: String?) {
        this.receiveUserModel()
    }

}