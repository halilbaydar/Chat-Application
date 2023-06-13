package com.chat.filter;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class JwtTokenVerifier implements WebFilter {
    private static final String DEFAULT_ROLE_PREFIX = "ROLE_";
    private static final String DEFAULT_SCOPE_PREFIX = "SCOPE_";
    private static final String ROLES_CLAIM = "authorities";
    private static final String SCOPED_ROLES_CLAIM = "scoped-authorities";

    private static final String USERNAME = "username";

    private final UserDetailsService userDetailsService;

    @SneakyThrows
    public static void getExceptionResponse(final ServerWebExchange response, final HttpStatus httpStatus) {
        response.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        var map = new Multip<String, Object>();
        map.put("code", httpStatus);
        map.put("status", HttpStatus.FORBIDDEN);
        response.getResponse().getHeaders().addAll(map);
    }

    @NotNull
    private static Stream<SimpleGrantedAuthority> getAuthorities(final ServerHttpRequest serverHttpRequest) {
        return Arrays.stream(Optional.ofNullable(serverHttpRequest
                                .getHeaders().get(ROLES_CLAIM))
                        .map(claim -> claim.)
                        .map(claim -> claim))
                .map(SimpleGrantedAuthority::new);
    }

    @NotNull
    private static Stream<SimpleGrantedAuthority> getScopedAuthorities(final ServerHttpRequest httpServletRequest) {
        return Arrays.stream(Optional
                        .ofNullable(httpServletRequest
                                .getHeaders().getFirst(SCOPED_ROLES_CLAIM)
                        ).map(scopedRoles -> scopedRoles.split(","))
                        .orElse(new String[0])
                )
                .map(SimpleGrantedAuthority::new);
    }

    @NotNull
    private static List<GrantedAuthority> getCombinedAuthorities(final ServerHttpRequest serverHttpRequest) {
        return Stream.concat(getAuthorities(serverHttpRequest),
                        getScopedAuthorities(serverHttpRequest))
                .collect(Collectors.toList());
    }

    private Authentication extractAuthentication(final ServerHttpRequest serverHttpRequest) {
        String username = Optional.ofNullable(serverHttpRequest.getHeaders().getFirst(USERNAME)).orElse("");
        return Optional.of(userDetailsService.loadUserByUsername(username)).map(userDetails -> {
            return new UsernamePasswordAuthenticationToken(userDetails, null, getCombinedAuthorities(serverHttpRequest));
        }).orElseThrow(() -> new BadCredentialsException("User could not be found!"));
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        try {
            final Authentication authentication = extractAuthentication(exchange.getRequest());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            getExceptionResponse(exchange, HttpStatus.UNAUTHORIZED);
            return Mono.empty();
        }
        chain.filter(exchange);
        return Mono.empty();
    }
}