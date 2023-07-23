package com.chat.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import static org.springframework.data.redis.core.RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP;

@Configuration
@ServletComponentScan
@EnableDiscoveryClient
@EnableAspectJAutoProxy
@EnableRedisRepositories(enableKeyspaceEvents = ON_STARTUP, keyspaceNotificationsConfigParameter = "")
@EntityScan(basePackages = {"com.chat.model.entity"})
@EnableMongoRepositories(basePackages = "com.chat.interfaces.repository")
public class AppConfig {
}
