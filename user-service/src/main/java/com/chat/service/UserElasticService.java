package com.chat.service;

import com.chat.config.KafkaConfigData;
import com.chat.kafka.avro.model.UserAvroModel;
import com.chat.model.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.SenderResult;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserElasticService {
    private final ReactiveKafkaProducerTemplate<String, UserAvroModel> userToElasticProducerTemplate;
    private final KafkaConfigData kafkaConfigData;

    public Mono<SenderResult<Void>> send(UserEntity userEntity) {
        return this.userToElasticProducerTemplate.send(
                kafkaConfigData.getTopicName(),
                userEntity.getId().toString(),
                UserAvroModel.newBuilder()
                        .setUsername(userEntity.getUsername())
                        .setName(userEntity.getName())
                        .setCreatedDate(new Date().getTime())
                        .setId(userEntity.getId().toString())
                        .build());
    }
}
