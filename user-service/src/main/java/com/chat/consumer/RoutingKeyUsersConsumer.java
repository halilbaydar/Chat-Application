package com.chat.consumer;

import com.chat.interfaces.repository.UserRepository;
import com.chat.model.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoutingKeyUsersConsumer implements RMessageConsumer<String, Object> {
    private final UserRepository userRepository;
    private final RedissonReactiveClient redissonReactiveClient;

    @Override
    @UserRequestsListener
    public Object consume(String username) throws RuntimeException {
        var userCache = redissonReactiveClient.getMapCache("cache:user",
                new TypedJsonJacksonCodec(String.class, UserDto.class));
        var dbUser = this.userRepository.findByUsername(username)
                .filter(Objects::nonNull)
                .map(UserDto::fromView)
                .flatMap(user -> userCache.fastPut(username, user, 30, TimeUnit.SECONDS)
                        .thenReturn(user));
        return userCache.get(userCache)
                .filter(Objects::nonNull)
                .switchIfEmpty(dbUser)
                .doOnError(error -> {
                    log.error("Error while responding request from auth service: %s", error);
                })
                .doOnSuccess(rabbitUserEntity -> {
                    log.info("Response sent successfully for username: ${}", username);
                })
                .block();
    }
}
