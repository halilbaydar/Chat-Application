package com.chat;

import com.chat.client.KafkaAdminClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
@SpringBootApplication(scanBasePackages = "com.chat")
public class UserServiceApplication {
    private KafkaAdminClient kafkaAdminClient;

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

    @PostConstruct
    public void checkKafkaUserServiceTopics() {
        kafkaAdminClient.createTopics();
        kafkaAdminClient.checkSchemaRegistry();
    }
}
