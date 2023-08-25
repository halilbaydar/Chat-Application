package com.chat.config;

import com.chat.properties.AuthCorsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.WebListenerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class CorsConfig implements WebMvcConfigurer{
    private final AuthCorsProperties authCorsProperties;

    @Bean
    public WebMvcConfigurer corsConfigurerForProd() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(final CorsRegistry registry) {
                registry.addMapping(authCorsProperties.getMapping())
                        .allowedOrigins(authCorsProperties.getOrigins())
                        .allowCredentials(false)
                        .allowedMethods(authCorsProperties.getMethods())
                        .allowedHeaders(authCorsProperties.getAllowedHeaders())
                        .exposedHeaders(authCorsProperties.getExposedHeaders());
            }
        };
    }
}
