package com.chat.kafka.producer.impl;

import com.chat.config.UserLogger;
import com.chat.kafka.avro.model.UserAvroModel;
import com.chat.kafka.producer.KafkaProducer;
import com.chat.model.entity.UserEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.ZoneId;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class KafkaUserRegisterEventProducer implements KafkaProducer<UserEntity, Mono<Void>> {
    private final ReactiveKafkaProducerTemplate<String, String> reactiveKafkaUserProducerTemplate;
    private final UserLogger LOG;
    private final ObjectMapper objectMapper;

    @Value("${user-register-kafka-topic}")
    private String topic;

    @SneakyThrows
    @Override
    public Mono<Void> send(final String traceId, final UserEntity userEntity) {
//        final var userAvroModel = UserAvroModel.newBuilder()
//                .setId(userEntity.getId())
//                .setCreatedAt(userEntity.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
//                .setName(userEntity.getName())
//                .setUsername(userEntity.getUsername())
//                .build();

        var user = objectMapper.writeValueAsString(userEntity);
        var headers = new ArrayList<Header>();
        headers.add(new RecordHeader("traceId", traceId.getBytes()));

        var produceRecord = new ProducerRecord<>(topic, null, userEntity.getUsername(), user, headers);

        return reactiveKafkaUserProducerTemplate.send(produceRecord)
                .doOnSuccess(senderResult -> {
                    LOG.register.info("User sent to kafka successfully after registration");
                })
                .doOnError(throwable -> {
                    LOG.register.error("Failed to send user to kafka topic after registration", throwable);
                })
                .then();
    }
}
