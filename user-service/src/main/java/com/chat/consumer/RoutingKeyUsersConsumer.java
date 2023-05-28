package com.chat.consumer;

import com.chat.interfaces.repository.UserRepository;
import com.chat.model.message.RabbitUserEntity;
import com.chat.redis.RedisStorageManager;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class RoutingKeyUsersConsumer {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private final RabbitTemplate rabbitTemplate;
    private final UserRepository userRepository;
    private final RedisStorageManager redisStorageManager;
    private final RabbitProperties rabbitProperties;

    @UserRequestsListener
    public Object provideUserToOtherServices(String username) {
        Object user = redisStorageManager.map.get("users", username);
        if (user != null) {
            return user;
        } else {
            return this.userRepository.findByUsername(username)
                    .filter(Objects::nonNull)
                    .map(userEntity -> RabbitUserEntity.
                            builder()
                            .id(userEntity.getId())
                            .status(userEntity.getStatus())
                            .name(userEntity.getName())
                            .username(userEntity.getUsername())
                            .role(userEntity.getRole())
                            .password(userEntity.getPassword())
                            .updatedAt(new Date().getTime())
                            .deletedAt(new Date().getTime())
                            .createdAt(new Date().getTime())
                            .build())
                    .doOnNext(userEntity -> {
                        redisStorageManager.map.put("users", username, userEntity);
                    })
                    .doOnError(error -> {
                        logger.error("Error while responding request from auth service: %s", error);
                    })
                    .doOnSuccess(rabbitUserEntity -> {
                        logger.info("Response sent successfully for username: ${}", rabbitUserEntity.getUsername());
                    }).block();
        }
    }
}
