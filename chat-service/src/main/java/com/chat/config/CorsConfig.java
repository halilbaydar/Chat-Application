package com.chat.config;

import com.chat.property.CorsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class CorsConfig implements WebMvcConfigurer {
    private static final String MAPPING = "/**";
    private static final String[] METHODS = {"GET", "POST"};

    private final CorsProperties corsProperties;

    @Bean
    public WebMvcConfigurer corsConfigurerForProd() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(final CorsRegistry registry) {
                registry.addMapping(MAPPING)
                        .allowedOrigins("*")
                        .allowCredentials(false)
                        .allowedMethods(METHODS)
                        .allowedHeaders(corsProperties.getAllowedHeaders())
                        .exposedHeaders(corsProperties.getExposedHeaders());
            }
        };
    }
}