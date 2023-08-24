package com.chat.config;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableRetry
@EnableWebFlux
@Configuration
@EnableDiscoveryClient
public class AppConfig {
}
