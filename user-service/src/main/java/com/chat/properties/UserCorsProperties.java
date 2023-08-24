package com.chat.properties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "config.cors")
public class UserCorsProperties {
    private String mapping;
    private String[] methods;
    private String[] origins;
    private boolean allowedCredentials;
    private String[] allowedHeaders;
    private String[] exposedHeaders;
}
