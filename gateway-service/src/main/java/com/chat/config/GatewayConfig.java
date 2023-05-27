package com.chat.config;

import com.chat.filter.AuthenticationFilter;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Objects;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

    private final RateLimiterConfigData rateLimitConfigData;
    private final AuthenticationFilter authenticationFilter;

    @Bean(name = "authHeaderResolver")
    public KeyResolver userKeyResolver() {
        return exchange -> Mono.just(Objects.requireNonNull(Objects.requireNonNull(exchange
                .getRequest().getRemoteAddress()).getAddress().getHostAddress()));
    }

    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> circuitBreakerFactoryCustomizer() {
        return reactiveResilience4JCircuitBreakerFactory ->
                reactiveResilience4JCircuitBreakerFactory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                        .timeLimiterConfig(TimeLimiterConfig.custom()
                                .timeoutDuration(Duration.ofMillis(rateLimitConfigData.getTimeoutMs()))
                                .build())
                        .circuitBreakerConfig(CircuitBreakerConfig.custom()
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

    @Bean
    public RedisRateLimiter redisRateLimiter() {
        return new RedisRateLimiter(rateLimitConfigData.getDefaultReplenishRate(),
                rateLimitConfigData.getDefaultBurstCapacity(),
                rateLimitConfigData.getDefaultRequestedTokens());
    }

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder
                .routes()
                .route(p -> p
                        .path("/chat/**")
                        .filters(gatewayFilterSpec -> getGatewayFilterSpec(gatewayFilterSpec, "chat", "chat")
                        )
                        .uri("lb://chat-service")
                )
                .route(p -> p
                        .path("/user/**")
                        .filters(gatewayFilterSpec -> getGatewayFilterSpec(gatewayFilterSpec, "user", "user")
                        )
                        .uri("lb://user-service")
                )
                .route(p -> p
                        .path("/search/**")
                        .filters(gatewayFilterSpec -> getGatewayFilterSpec(gatewayFilterSpec, "search", "search"))
                        .uri("lb://search-service")
                )
                .route(p -> p
                        .path("/auth/**")
                        .filters(gatewayFilterSpec -> getGatewayFilterSpec(gatewayFilterSpec, "auth", "auth"))
                        .uri("lb://auth-service")
                )
                .build();
    }

    private GatewayFilterSpec getGatewayFilterSpec(GatewayFilterSpec gatewayFilterSpec, String fallback, String pathReplace) {
        return gatewayFilterSpec
                .filter(authenticationFilter)
                .requestRateLimiter(config -> {
                    config.setDenyEmptyKey(false);
                    config.setKeyResolver(userKeyResolver());
                    config.setRateLimiter(redisRateLimiter());
                })
                .retry(retryConfig -> {
                    retryConfig.setBackoff(Duration.ofMillis(100), Duration.ofMillis(1000), 2, Boolean.TRUE);
                }).saveSession()
                .saveSession()
                .rewritePath(String.format("/%s(?<segment>/?.*)", pathReplace), "$\\{segment}")
                .circuitBreaker(config -> {
                    config.setFallbackUri(String.format("/fallback/%s-fallback", fallback));
                    config.setName("chatCircuitBreaker");
                });
    }
}
