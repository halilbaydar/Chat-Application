package com.chat.config;

import com.chat.filter.JwtTokenVerifier;
import com.chat.interfaces.service.JwtService;
import com.chat.model.common.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

import java.util.EventListener;

@Order(1)
@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

    private final JwtService jwtService;
    private final FindByIndexNameSessionRepository<? extends Session> sessionRepository;

    @Bean
    public static <T extends EventListener> ServletListenerRegistrationBean<T> httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean<T>((T) new HttpSessionEventPublisher());
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http.cors()
                .and()
                .csrf().disable()// TODO: Enabeble this in production
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .addFilterBefore(new JwtTokenVerifier(jwtService), BasicAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/v1/register/**", "/swagger-ui/**", "/ws/**", "/app/**", "/login/***").permitAll()
                .antMatchers("/user/**").hasAnyRole(Role.USER.name())
                .anyRequest().fullyAuthenticated()
                .and().build();
    }

    @Bean
    public SpringSessionBackedSessionRegistry<? extends Session> sessionRegistry() {
        return new SpringSessionBackedSessionRegistry<>(sessionRepository);
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web
                .ignoring()
                .antMatchers("/v1/register/**", "/ws/**", "/app/**");
    }
}