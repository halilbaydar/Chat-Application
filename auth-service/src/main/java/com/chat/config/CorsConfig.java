package com.chat.config;

import com.chat.properties.AuthCorsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class CorsConfig implements WebMvcConfigurer {
    private final AuthCorsProperties authCorsProperties;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(authCorsProperties.getMapping())
                .allowedOrigins(authCorsProperties.getOrigins())
                .allowCredentials(authCorsProperties.isAllowedCredentials())
                .allowedMethods(authCorsProperties.getMethods())
                .allowedHeaders(authCorsProperties.getAllowedHeaders())
                .exposedHeaders(authCorsProperties.getExposedHeaders());
    }
}
