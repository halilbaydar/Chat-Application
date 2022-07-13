package com.chat.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "cors")
public class CorsProperties {
    private String[] origins;
    private String[] allowedHeaders;
    private String[] exposedHeaders;
}
