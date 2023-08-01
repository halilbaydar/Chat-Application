package com.chat.config;

import com.chat.auth.ChatReactiveUserDetailsService;
import com.chat.filter.PreAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.ReactivePreAuthenticatedAuthenticationManager;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.web.server.WebSession;

@Configuration
@RequiredArgsConstructor
@EnableReactiveMethodSecurity
public class WebSecurityConfig {
    private final ChatReactiveUserDetailsService chatReactiveUserDetailsService;
    private final ServerSecurityContextRepository serverSecurityContextRepository;

    @Bean
    public SecurityWebFilterChain webFluxSecurityConfig(ServerHttpSecurity http) {
        return http
                .logout(logout -> logout.logoutHandler((exchange, authentication) ->
                        exchange.getExchange().getSession()
                                .map(WebSession::invalidate)
                                .then()))
                .authorizeExchange()
                .anyExchange()
                .authenticated()
                .and()
                .addFilterAfter(new PreAuthenticationFilter(), SecurityWebFiltersOrder.AUTHORIZATION)
                .csrf()
                .disable()
                .securityContextRepository(serverSecurityContextRepository)
                .build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/v1/register/**");
    }

    @Bean
    public ReactivePreAuthenticatedAuthenticationManager reactiveAuthenticationManager() {
        var reactivePreAuthenticatedAuthenticationManager = new ReactivePreAuthenticatedAuthenticationManager(chatReactiveUserDetailsService);
        return reactivePreAuthenticatedAuthenticationManager;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        var daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPreAuthenticationChecks();

        return daoAuthenticationProvider;
    }
}
