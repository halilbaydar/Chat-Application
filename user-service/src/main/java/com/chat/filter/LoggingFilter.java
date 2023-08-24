package com.chat.filter;

import org.apache.logging.log4j.ThreadContext;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.UUID;

public class LoggingFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ThreadContext.put("traceId", UUID.randomUUID().toString());
        return chain.filter(exchange);
    }
}
