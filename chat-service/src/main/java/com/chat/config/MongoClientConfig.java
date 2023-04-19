package com.chat.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoClientConfig {
    private final Object lock = new Object();
    private MongoClient mongoClient;

    @Value("${spring.data.mongodb.uri}")
    private String uri;

    @Bean
    public MongoClient mongoClient() {
        synchronized (lock) {
            if (mongoClient == null) {
                mongoClient = MongoClients.create(uri);
            }
            return mongoClient;
        }
    }
}
