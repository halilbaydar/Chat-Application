package com.chat.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Configuration
public class KeyResolverConfig {

    @Bean(name = "authHeaderResolver")
    public KeyResolver userKeyResolver() {
        return exchange -> Mono.just(Objects.requireNonNull(Objects.requireNonNull(exchange
                .getRequest().getRemoteAddress()).getAddress().getHostAddress()));
    }
}
