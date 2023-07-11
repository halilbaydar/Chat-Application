package com.chat.config;

import com.chat.property.RateLimiterConfigData;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.HttpServerErrorException;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeoutException;

@Configuration
@RequiredArgsConstructor
public class ChatCircuitBreakerConfig {
    private final RateLimiterConfigData rateLimitConfigData;

    @Bean
    public TimeLimiterConfig timeLimiterConfig() {
        return TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofMillis(rateLimitConfigData.getTimeoutMs()))
                .build();
    }

    @Bean
    public CircuitBreakerConfig circuitBreakerConfig() {
        return CircuitBreakerConfig.custom()
                .failureRateThreshold(rateLimitConfigData.getFailureRateThreshold())
                .slowCallRateThreshold(rateLimitConfigData.getSlowCallRateThreshold())
                .slowCallDurationThreshold(Duration.ofMillis(rateLimitConfigData
                        .getSlowCallDurationThreshold()))
                .permittedNumberOfCallsInHalfOpenState(rateLimitConfigData
                        .getPermittedNumOfCallsInHalfOpenState())
                .slidingWindowSize(rateLimitConfigData.getSlidingWindowSize())
                .minimumNumberOfCalls(rateLimitConfigData.getMinNumberOfCalls())
                .waitDurationInOpenState(Duration.ofMillis(rateLimitConfigData
                        .getWaitDurationInOpenState()))
                .enableAutomaticTransitionFromOpenToHalfOpen()
                .recordException(throwable -> throwable instanceof HttpServerErrorException.InternalServerError)
                .recordExceptions(IOException.class, TimeoutException.class)
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .build();
    }

    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory>
    circuitBreakerFactoryCustomizer(CircuitBreakerConfig circuitBreakerConfig, TimeLimiterConfig timeLimiterConfig) {
        return (reactiveResilience4JCircuitBreakerFactory) -> {
            reactiveResilience4JCircuitBreakerFactory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                    .timeLimiterConfig(timeLimiterConfig)
                    .circuitBreakerConfig(circuitBreakerConfig)
                    .build());
        };
    }

    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry(CircuitBreakerConfig circuitBreakerConfig) {
        return CircuitBreakerRegistry.of(circuitBreakerConfig);
    }
}
