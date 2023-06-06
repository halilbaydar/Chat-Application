package com.chat.consumer;

import com.chat.interfaces.repository.UserRepository;
import com.chat.model.RabbitUserEntity;
import com.chat.model.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Date;
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
                new TypedJsonJacksonCodec(String.class, UserEntity.class));
        return userCache.get(userCache)
                .filter(Objects::nonNull)
                .switchIfEmpty(this.userRepository.findByUsername(username)
                        .filter(Objects::nonNull)
                        .map(userEntity -> new RabbitUserEntity(
                                userEntity.getId(),
                                userEntity.getUsername(),
                                userEntity.getName(),
                                userEntity.getRole(),
                                userEntity.getStatus(),
                                userEntity.getPassword(),
                                new Date().getTime(),
                                new Date().getTime(),
                                new Date().getTime()))
                        .flatMap(user -> userCache.fastPut(username, user, 30, TimeUnit.SECONDS)
                                .thenReturn(user))
                )
                .doOnError(error -> {
                    logger.error("Error while responding request from auth service: %s", error);
                })
                .doOnSuccess(rabbitUserEntity -> {
                    logger.info("Response sent successfully for username: ${}", username);
                })
                .block();
    }
}
