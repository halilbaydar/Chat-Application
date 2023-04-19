package com.chat.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Objects;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

    private final RateLimiterConfigData rateLimitConfigData;

    @Bean(name = "authHeaderResolver")
    public KeyResolver userKeyResolver() {
        return exchange -> Mono.just(Objects
                .requireNonNull(Objects.requireNonNull(
                        exchange.getRequest().getRemoteAddress()).getAddress().getHostAddress()));
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
        return new RedisRateLimiter(5, 10, 1);
    }

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder
                .routes()
                .route(p -> p
                        .path("/my-name/**")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker(config -> {
                                config.setFallbackUri("/fallback/myname-fallback");
                                config.setName("chatCircuitBreaker");
                            });
                            gatewayFilterSpec.requestRateLimiter(config -> {
                                config.setDenyEmptyKey(false);
                                config.setKeyResolver(userKeyResolver());
                                config.setRateLimiter(redisRateLimiter());
                            });
                            return gatewayFilterSpec.filter(((exchange, chain) -> {
                                ServerHttpRequest req = exchange.getRequest();
                                String path = req.getURI().getRawPath();
                                String newPath = path.replaceFirst("/my-name", "");
                                ServerHttpRequest request = req.mutate().path(newPath).build();
                                exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                return chain.filter(exchange.mutate().request(request).build());
                            }));
                        })
                        .uri("lb://my-name")
                ).build();
    }
}
