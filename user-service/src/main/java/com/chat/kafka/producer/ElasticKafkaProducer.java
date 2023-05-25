package com.chat.kafka.producer;

import com.chat.config.KafkaConfigData;
import com.chat.kafka.avro.model.UserAvroModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Date;
import java.util.UUID;

@Service
public class ElasticKafkaProducer {
    private final ReactiveKafkaProducerTemplate<String, UserAvroModel> reactiveKafkaProducerTemplate;
    private final KafkaConfigData kafkaConfigData;

    public ElasticKafkaProducer(
            @Qualifier("user-to-elastic-producer-template")
            ReactiveKafkaProducerTemplate<String, UserAvroModel> reactiveKafkaProducerTemplate, KafkaConfigData kafkaConfigData) {
        this.reactiveKafkaProducerTemplate = reactiveKafkaProducerTemplate;
        this.kafkaConfigData = kafkaConfigData;
    }

    public void run() {
        Flux.range(0, 1)
                .map(
                        index -> UserAvroModel
                                .newBuilder()
                                .setId(UUID.randomUUID().toString())
                                .setName("random-name")
                                .setUsername("username-1")
                                .setCreatedDate(new Date().getTime())
                                .build()
                )
                .doOnNext(it -> System.out.println("Data: $it"))
                .flatMap(it -> this.reactiveKafkaProducerTemplate.send(kafkaConfigData.getTopicName(), it.getId(), it))
                .doOnError(it -> System.out.printf("Error: %s%n", it.getMessage()))
                .doOnNext(it -> System.out.printf("Result:  %s%n", it.recordMetadata().toString()))
                .subscribe();
    }
}
