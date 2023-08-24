package com.chat.kafka.producer;

import com.chat.kafka.avro.model.UserAvroModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import reactor.kafka.sender.SenderOptions;

@Configuration
public class ElasticKafkaProducerConfig {

    @Bean("user-register-producer-option")
    public SenderOptions<String, String> senderOps(KafkaProperties kafkaProperties) {
        return SenderOptions.create(kafkaProperties.buildProducerProperties());
    }

    @Bean("user-register-producer-template")
    public ReactiveKafkaProducerTemplate<String, String> reactiveKafkaUserProducerTemplate(
            @Qualifier("user-register-producer-option")
            SenderOptions<String, String> senderOptions) {
        return new ReactiveKafkaProducerTemplate<>(senderOptions);
    }
}
