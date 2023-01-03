package com.chat.config;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import lombok.RequiredArgsConstructor;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.stereotype.Component;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Component
@RequiredArgsConstructor
public class MongoDatabaseConfig {
    private MongoClient mongoClient;
    private MongoDatabase database;

    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    @Value("${spring.data.mongodb.uri}")
    private String uri;

    @Bean
    public MongoClient mongoClient() {
        if (mongoClient == null) {
            mongoClient = MongoClients.create(uri);
        }
        return mongoClient;
    }

    @Bean
    public CodecRegistry pojoCodecRegistry() {
        return fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(
                        PojoCodecProvider
                                .builder()
                                .automatic(true)
                                .build()
                )
        );
    }

    @Bean
    public MongoDatabase database() {

        if (database != null)
            return database;

        CodecRegistry pojoCodecRegistry = fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(
                        PojoCodecProvider
                                .builder()
                                .automatic(true)
                                .build()
                )
        );

        database = mongoClient
                .getDatabase(databaseName)
                .withCodecRegistry(pojoCodecRegistry);

        return database;
    }

    @Bean
    public MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }
}
