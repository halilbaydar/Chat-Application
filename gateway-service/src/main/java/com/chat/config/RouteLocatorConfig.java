package com.chat.config;

import com.chat.filter.AuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class RouteLocatorConfig {

    private final AuthenticationFilter authenticationFilter;
    private final RedisRateLimiter redisRateLimiter;
    private final KeyResolver authHeaderResolver;

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
                    config.setKeyResolver(authHeaderResolver);
                    config.setRateLimiter(redisRateLimiter);
                })
                .retry(retryConfig -> {
                    retryConfig.setBackoff(Duration.ofMillis(100), Duration.ofMillis(1000), 2, Boolean.TRUE);
                }).saveSession()
                .rewritePath(String.format("/%s(?<segment>/?.*)", pathReplace), "$\\{segment}")
                .circuitBreaker(config -> {
                    config.setFallbackUri(String.format("/fallback/%s-fallback", fallback));
                    config.setName("chatCircuitBreaker");
                });
    }
}
