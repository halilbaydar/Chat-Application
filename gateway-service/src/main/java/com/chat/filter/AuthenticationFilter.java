package com.chat.filter;

import com.chat.constant.HttpConstant;
import com.chat.util.AuthUtil;
import com.chat.util.RouterValidator;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RefreshScope
@RequiredArgsConstructor
public class AuthenticationFilter implements GatewayFilter {
    private final AuthUtil jwtUtil;
    private final RouterValidator routerValidator;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (routerValidator.isSecured.test(request)) {
            if (this.isAuthMissing(request))
                return this.onError(exchange, "Authorization header is missing in request", HttpStatus.UNAUTHORIZED);

            final String token = this.getAuthHeader(request);
            Claims claims = jwtUtil.getAllClaimsFromToken(token);
            if (jwtUtil.isInvalid(claims))
                return this.onError(exchange, "Authorization header is invalid", HttpStatus.UNAUTHORIZED);

            this.populateRequestWithHeaders(exchange, claims);
        }
        return chain.filter(exchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        log.error(err);
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        response.getHeaders().add("errorMessage", err);
        return response.setComplete();
    }

    private String getAuthHeader(ServerHttpRequest request) {
        return CollectionUtils.firstElement(request.getHeaders().getOrEmpty(HttpConstant.AUTHORIZATION));
    }

    private boolean isAuthMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey(HttpConstant.AUTHORIZATION);
    }

    private void populateRequestWithHeaders(ServerWebExchange exchange, Claims claims) {
        List<SimpleGrantedAuthority> grantedAuthorities = jwtUtil.getGrantedAuthorities(claims);
        String username = claims.getSubject();
        exchange.getRequest().mutate()
                .header("username", username)
                .header("authorities", Arrays.toString(grantedAuthorities.toArray()))
                .build();
    }
}
