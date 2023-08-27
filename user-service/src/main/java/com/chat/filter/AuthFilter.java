package com.chat.filter;

import lombok.NonNull;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class AuthFilter implements WebFilter {
    private static final String PRINCIPAL = "username";
    private static final String CREDENTIALS = "authorities";
    private static final String ROLE_PREFIX = "ROLE_";

    @Override
    public @NonNull Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        var username = exchange.getRequest().getHeaders().getFirst(PRINCIPAL);
        var authorities = exchange.getRequest().getHeaders().getFirst(CREDENTIALS);
        if (username == null || authorities == null) {
            return Mono.empty();
        }

        var context = ReactiveSecurityContextHolder.getContext();
        var grantedAuthorities = Arrays.stream(authorities.split(","))
                .map(this::rolePrefix)
                .filter(Objects::nonNull)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return context.doOnNext(securityContext -> {
            securityContext.setAuthentication(
                    new PreAuthenticatedAuthenticationToken(username, null, grantedAuthorities)
            );
        }).then(chain.filter(exchange));
    }

    private String rolePrefix(String role) {
        if (role == null) {
            return null;
        }

        if (role.startsWith("ROLE_")) {
            return role;
        }

        return ROLE_PREFIX.concat(role);
    }
}
