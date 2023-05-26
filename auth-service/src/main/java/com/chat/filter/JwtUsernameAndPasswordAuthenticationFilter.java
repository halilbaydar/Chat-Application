package com.chat.filter;

import com.chat.auth.JwtConfig;
import com.chat.auth.JwtUtilImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtConfig jwtConfig;
    private final JwtUtilImpl jwtService;

    public JwtUsernameAndPasswordAuthenticationFilter(final JwtConfig jwtConfig, final JwtUtilImpl jwtService, AuthenticationManager authenticationManager) {
        super(authenticationManager);
        this.jwtConfig = jwtConfig;
        this.jwtService = jwtService;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) {
        String token = jwtService.generateTokenForLogin(authResult.getName(), authResult.getAuthorities());
        response.addHeader(jwtConfig.getAuthorizationHeader(), jwtConfig.getTokenPrefix() + token);
    }
}
