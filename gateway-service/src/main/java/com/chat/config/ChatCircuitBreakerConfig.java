package com.chat.config;

import com.chat.property.RateLimiterConfigData;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class CircuitBreakerConfig {
    private final RateLimiterConfigData rateLimitConfigData;

    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> circuitBreakerFactoryCustomizer() {
        return reactiveResilience4JCircuitBreakerFactory ->
                reactiveResilience4JCircuitBreakerFactory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                        .timeLimiterConfig(TimeLimiterConfig.custom()
                                .timeoutDuration(Duration.ofMillis(rateLimitConfigData.getTimeoutMs()))
                                .build())
                        .circuitBreakerConfig(io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.custom()
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
                                .build())
                        .build());
    }
}
