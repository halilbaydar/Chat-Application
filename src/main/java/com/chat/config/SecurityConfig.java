package com.chat.config;

import com.chat.filter.JwtTokenVerifier;
import com.chat.interfaces.service.JwtService;
import com.chat.model.common.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

import javax.crypto.SecretKey;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;
    private final JwtService jwtService;

    @Autowired
    private FindByIndexNameSessionRepository<? extends Session> sessionRepository;

    @Bean
    public static ServletListenerRegistrationBean httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean(new HttpSessionEventPublisher());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                .csrf().disable()// TODO: Enabeble this in production
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .addFilterBefore(new JwtTokenVerifier(jwtConfig, jwtService), BasicAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/home/**", "/swagger-ui/**", "/ws/**", "/app/**").permitAll()
                .antMatchers("/user/**").hasAnyRole(Role.USER.name())
        ;

    }

    @Bean
    public SpringSessionBackedSessionRegistry<? extends Session> sessionRegistry() {
        return new SpringSessionBackedSessionRegistry<>(sessionRepository);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/app/**", "/ws/**", "/home/**");
    }
}