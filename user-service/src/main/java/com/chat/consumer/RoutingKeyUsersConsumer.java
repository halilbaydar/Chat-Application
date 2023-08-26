package com.chat.consumer;

import com.chat.interfaces.repository.UserRepository;
import com.chat.model.dto.UserDto;
import com.chat.model.view.UserView;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RoutingKeyUsersConsumer implements RMessageConsumer<String, Object> {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
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
                    logger.error("Error while responding request from auth service: %s", error);
                })
                .doOnSuccess(rabbitUserEntity -> {
                    logger.info("Response sent successfully for username: ${}", username);
                })
                .block();
    }
}
