package com.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import static org.springframework.data.redis.core.RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP;

@Configuration
@ServletComponentScan
@EnableDiscoveryClient
@EnableRedisRepositories(enableKeyspaceEvents = ON_STARTUP, keyspaceNotificationsConfigParameter = "")
@EntityScan(basePackages = {"com.chat.model.entity"})
@EnableMongoRepositories(basePackages = "com.chat.interfaces.repository")
@SpringBootApplication(exclude = {MongoAutoConfiguration.class})
public class ChatServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChatServiceApplication.class, args);
    }
}