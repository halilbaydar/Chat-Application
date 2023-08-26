package com.chat.security;

import com.chat.filter.LoggingFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.ReactivePreAuthenticatedAuthenticationManager;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.web.server.WebSession;

@Configuration
@RequiredArgsConstructor
@EnableReactiveMethodSecurity
public class WebSecurityConfig {
    private final ReactiveUserDetailsService chatReactiveUserDetailsService;
    private final ServerSecurityContextRepository redissonServerSecurityContextRepository;

    @Bean
    public SecurityWebFilterChain webFluxSecurityConfig(ServerHttpSecurity http) {
        return http
                .logout(logout -> logout.logoutHandler((exchange, authentication) ->
                        exchange.getExchange().getSession()
                                .map(WebSession::invalidate)
                                .then()))
                .authorizeExchange()
                .pathMatchers(HttpMethod.POST,"/v1/register").permitAll()
                .pathMatchers(HttpMethod.POST,"/login").permitAll()
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                .anyExchange()
                .authenticated()
                .and()
                .addFilterAfter(new LoggingFilter(), SecurityWebFiltersOrder.FIRST)
                .csrf()
                .disable()
//                .securityContextRepository(redissonServerSecurityContextRepository)
                .build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/v1/register/**");
    }

    @Bean
    public ReactivePreAuthenticatedAuthenticationManager reactiveAuthenticationManager() {
        return new ReactivePreAuthenticatedAuthenticationManager(chatReactiveUserDetailsService);
    }
}
