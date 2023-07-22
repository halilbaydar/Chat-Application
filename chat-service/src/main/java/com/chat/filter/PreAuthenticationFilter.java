package com.chat.filter;

import com.chat.exception.TokenNotFoundException;
import lombok.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedGrantedAuthoritiesUserDetailsService;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PreAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {
    public PreAuthenticationFilter() {
        this.setAuthenticationManager(getAuthenticationManager());
    }

    @NonNull
    private static AuthenticationManager getAuthenticationManager() {
        return authentication -> {
            var preAuthProvider = new PreAuthenticatedAuthenticationProvider();
            preAuthProvider.setPreAuthenticatedUserDetailsService(PreAuthenticationUserDetailServiceFactory
                    .authenticationUserDetailsService());
            return (Authentication) new ProviderManager(List.of(preAuthProvider));
        };
    }

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        return request.getHeader("username");
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return Arrays.stream(Optional.ofNullable(request.getHeader("authorities"))
                        .map(auth -> auth.split(",")).orElseThrow(TokenNotFoundException::new))
                .map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }


    private static class PreAuthenticationUserDetailServiceFactory {
        protected static AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> authenticationUserDetailsService() {
            return new PreAuthenticatedGrantedAuthoritiesUserDetailsService();
        }
    }
}
