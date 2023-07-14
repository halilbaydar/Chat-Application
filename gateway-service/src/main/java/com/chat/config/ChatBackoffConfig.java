package com.chat.config;

import org.springframework.cloud.gateway.filter.factory.RetryGatewayFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class ChatBackoffConfig {

    @Bean
    public RetryGatewayFilterFactory.BackoffConfig backoffConfig() {
        var backoff = new RetryGatewayFilterFactory.BackoffConfig();
        backoff.setFirstBackoff(Duration.ofMillis(100));
        backoff.setMaxBackoff(Duration.ofMillis(1000));
        backoff.setFactor(2);
        backoff.setBasedOnPreviousValue(Boolean.TRUE);
        return backoff;
    }
}
