package com.chat.kafka.producer;
//
//import com.chat.kafka.avro.model.UserAvroModel;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
//import reactor.kafka.sender.SenderOptions;
//
//@Configuration
//public class ElasticKafkaProducerConfig {
//
//    @Bean("user-to-elastic-producer-option")
//    public SenderOptions<String, UserAvroModel> senderOps(KafkaProperties kafkaProperties) {
//        return SenderOptions.create(kafkaProperties.buildProducerProperties());
//    }
//
//    @Bean("user-to-elastic-producer-template")
//    public ReactiveKafkaProducerTemplate<String, UserAvroModel> reactiveKafkaProducerTemplate(
//            @Qualifier("user-to-elastic-producer-option")
//            SenderOptions<String, UserAvroModel> senderOptions) {
//        return new ReactiveKafkaProducerTemplate<>(senderOptions);
//    }
//}
