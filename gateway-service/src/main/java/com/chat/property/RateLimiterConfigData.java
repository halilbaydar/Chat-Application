package com.chat.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "gateway-service")
public class RateLimiterConfigData {
    private Long timeoutMs;
    private Float failureRateThreshold;
    private Float slowCallRateThreshold;
    private Long slowCallDurationThreshold;
    private Integer permittedNumOfCallsInHalfOpenState;
    private Integer slidingWindowSize;
    private Integer minNumberOfCalls;
    private Long waitDurationInOpenState;
    private Integer defaultReplenishRate;
    private Integer defaultBurstCapacity;
    private Integer defaultRequestedTokens;
}

