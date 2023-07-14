package com.chat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityWebFilterChain webFluxSecurityConfig(ServerHttpSecurity http) {
        return http
                .authorizeExchange()
                .anyExchange()
                .authenticated()
                .and()
                .csrf()
                .disable()
                .build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/v1/register/**");
    }
}
