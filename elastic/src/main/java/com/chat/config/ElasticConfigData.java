package com.chat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "elastic-config")
public class ElasticConfigData {
    private String users_index;
    private String connectionUrl;
    private Integer connectTimeoutMs;
    private Integer socketTimeoutMs;

    public String getUsersIndex() {
        return this.users_index;
    }
}
