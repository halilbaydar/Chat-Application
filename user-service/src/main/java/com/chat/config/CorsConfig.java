package com.chat.config;

import com.chat.properties.UserCorsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
@RequiredArgsConstructor
public class CorsConfig implements WebFluxConfigurer {
    private final UserCorsProperties userCorsProperties;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(userCorsProperties.getMapping())
                .allowedOrigins(userCorsProperties.getOrigins())
                .allowCredentials(userCorsProperties.isAllowedCredentials())
                .allowedMethods(userCorsProperties.getMethods())
                .allowedHeaders(userCorsProperties.getAllowedHeaders())
                .exposedHeaders(userCorsProperties.getExposedHeaders());
    }
}
