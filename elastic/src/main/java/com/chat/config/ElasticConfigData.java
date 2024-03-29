package com.chat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "elastic-config")
public class ElasticConfigData {
    private String connectionUrl;
    private String username;
    private String password;
    private Integer connectTimeoutMs;
    private Integer socketTimeoutMs;
}
