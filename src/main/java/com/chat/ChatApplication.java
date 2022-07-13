package com.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.data.redis.core.RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP;

@Configuration
@RestController
@ServletComponentScan
@EnableRedisRepositories(enableKeyspaceEvents = ON_STARTUP, keyspaceNotificationsConfigParameter = "")
@EntityScan(basePackages = {"com.chat.model.entity"})
@EnableMongoRepositories(basePackages = "com.chat.interfaces.repository")
@SpringBootApplication
public class ChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
    }

}
