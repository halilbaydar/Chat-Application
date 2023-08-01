package com.chat.config;

import com.chat.auth.JwtConfig;
import com.chat.auth.JwtUtilImpl;
import com.chat.filter.JwtUsernameAndPasswordAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import java.util.EventListener;

@Order(1)
@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

    private final JwtConfig jwtConfig;
    private final JwtUtilImpl jwtService;
//    private final FindByIndexNameSessionRepository<? extends Session> sessionRepository;

    @Bean
    public static <T extends EventListener> ServletListenerRegistrationBean<T> httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean<T>((T) new HttpSessionEventPublisher());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http,
                                            BCryptPasswordEncoder bCryptPasswordEncoder,
                                            UserDetailsService userDetailService)
            throws Exception {
        return http
                .cors()
                .and()
                .csrf()
                .disable()// TODO: Enable this in production
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(
                        jwtConfig,
                        jwtService,
                        authenticationManager(http,
                                bCryptPasswordEncoder,
                                userDetailService)))
                .authorizeRequests()
                .antMatchers("/v1/register/**", "/swagger-ui/**", "/ws/**", "/app/**", "/login/***")
                .permitAll()
                .antMatchers("/user/**")
                .fullyAuthenticated()
                .and()
                .build();
    }

//    @Bean
//    public SpringSessionBackedSessionRegistry<? extends Session> sessionRegistry() {
//        return new SpringSessionBackedSessionRegistry<>(sessionRepository);
//    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/v1/register/**", "/ws/**", "/app/**");
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailsService userDetailService) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).userDetailsService(userDetailService).passwordEncoder(bCryptPasswordEncoder).and().build();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }
}