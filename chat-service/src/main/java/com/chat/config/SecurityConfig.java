package com.chat.config;

import com.chat.filter.JwtTokenVerifier;
import com.chat.interfaces.repository.ChatSecurityContextRepository;
import com.chat.model.common.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
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
@Profile(value = {"development", "production"})
public class SecurityConfig {

    private final FindByIndexNameSessionRepository<? extends Session> sessionRepository;
    private final ChatSecurityContextRepository chatSecurityContextRepository;

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
        return http
                .sessionManagement(sessionConfig -> sessionConfig
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(true)
                ).cors()
                .and()
                .csrf()
                .disable()// TODO: Enable this in production
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .securityContext(contextConfigurer -> {
                    contextConfigurer.securityContextRepository(chatSecurityContextRepository);
                })
                .addFilterBefore(new JwtTokenVerifier(), BasicAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/swagger-ui/**", "/ws/**", "/app/**").permitAll()
                .antMatchers("/user/**").hasAnyRole(Role.USER.name())
                .anyRequest()
                .fullyAuthenticated()
                .and()
                .build();
    }

    @Bean
    public SpringSessionBackedSessionRegistry<? extends Session> sessionRegistry() {
        return new SpringSessionBackedSessionRegistry<>(sessionRepository);
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web
                .ignoring()
                .antMatchers("/ws/**", "/app/**");
    }
}