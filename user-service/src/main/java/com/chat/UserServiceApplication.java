package com.chat;

import com.chat.client.ChatKafkaAdminClient;
import com.chat.kafka.producer.ElasticKafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;

@Slf4j
@RequiredArgsConstructor
@SpringBootApplication(exclude = {RedisAutoConfiguration.class, RedissonAutoConfiguration.class})
public class UserServiceApplication implements CommandLineRunner {
    private final ChatKafkaAdminClient chatKafkaAdminClient;
    private final ElasticKafkaProducer elasticKafkaProducer;

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

    @Override
    public void run(String... args) {
//        chatKafkaAdminClient.createTopics();
//        chatKafkaAdminClient.checkSchemaRegistry();
        log.info("Topics generated successfully!!!!");
//        elasticKafkaProducer.run();
    }
}

