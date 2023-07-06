package com.chat.config;

import com.chat.property.RateLimiterConfigData;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RedisRateLimiterConfig {
    private final RateLimiterConfigData rateLimitConfigData;

    @Bean
    public RedisRateLimiter redisRateLimiter() {
        return new RedisRateLimiter(rateLimitConfigData.getDefaultReplenishRate(),
                rateLimitConfigData.getDefaultBurstCapacity(),
                rateLimitConfigData.getDefaultRequestedTokens());
    }
}
