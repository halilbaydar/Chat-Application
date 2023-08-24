package com.chat.config;

import com.chat.properties.CorsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
@RequiredArgsConstructor
public class CorsConfig implements WebFluxConfigurer {
    private final CorsProperties corsProperties;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(corsProperties.getMapping())
                .allowedOrigins(corsProperties.getOrigins())
                .allowCredentials(corsProperties.isAllowedCredentials())
                .allowedMethods(corsProperties.getMethods())
                .allowedHeaders(corsProperties.getAllowedHeaders())
                .exposedHeaders(corsProperties.getExposedHeaders());
    }
}
