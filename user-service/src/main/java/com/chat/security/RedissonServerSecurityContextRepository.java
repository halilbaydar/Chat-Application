package com.chat.security;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class RedissonServerSecurityContextRepository implements ServerSecurityContextRepository {
    private static final String DEFAULT_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";
    private static final String USERNAME = "username";

    private final RedissonReactiveClient redissonReactiveClient;

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        var mapCache = redissonReactiveClient.getMapCache(DEFAULT_SECURITY_CONTEXT, new TypedJsonJacksonCodec(String.class, PreAuthenticatedAuthenticationToken.class));
        var username = context.getAuthentication().getName();
        var put = mapCache.fastPut(username, context.getAuthentication());
        return put.then();
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        var mapCache = redissonReactiveClient.<String, PreAuthenticatedAuthenticationToken>getMapCache(DEFAULT_SECURITY_CONTEXT, new TypedJsonJacksonCodec(String.class, PreAuthenticatedAuthenticationToken.class));
        var key = exchange.getRequest().getHeaders().getFirst(USERNAME);
        var authentication = mapCache.get(key);
        return authentication.map(preAuthenticatedAuthenticationToken -> new SecurityContext() {
            @Override
            public Authentication getAuthentication() {
                return preAuthenticatedAuthenticationToken;
            }

            @Override
            public void setAuthentication(Authentication authentication) {

            }
        });
    }
}
