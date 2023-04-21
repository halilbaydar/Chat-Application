package com.chat.config;

import com.chat.aut.Role;
import com.chat.filter.JwtTokenFilter;
import com.chat.util.RouterValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;


@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .httpBasic().disable()
                .formLogin().disable()
                .authorizeExchange()
                .pathMatchers(RouterValidator.openApiEndpoints.toArray(String[]::new))
                .permitAll()
                .pathMatchers("/user/**").hasRole(Role.USER.name())
                .and().addFilterAt(jwtTokenFilter, SecurityWebFiltersOrder.AUTHORIZATION)
                .build();
    }
}
