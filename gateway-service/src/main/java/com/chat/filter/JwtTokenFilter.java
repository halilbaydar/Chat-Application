package com.chat.filter;

import com.chat.constant.HttpConstant;
import com.chat.util.JwtUtilImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenFilter implements WebFilter {
    private final JwtUtilImpl jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        var token = exchange.getRequest().getHeaders().getFirst(HttpConstant.AUTHORIZATION);
        var payloadOpt = jwtUtil.verify(token);
        if (payloadOpt.isPresent() && payloadOpt.get().isAvailable()) {
            var payload = payloadOpt.get();
            List<SimpleGrantedAuthority> simpleGrantedAuthorities = payloadOpt.get().getAuthorities().stream()
                    .map(m -> new SimpleGrantedAuthority(m.get("authority"))).collect(Collectors.toList());
            var authentication = new UsernamePasswordAuthenticationToken(payload.getUsername(),
                    null, simpleGrantedAuthorities);
            log.trace("Set security context {}", payload);
            return chain.filter(exchange)
                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
        }
        return chain.filter(exchange);
    }
}
