package com.chat.consumer;

import com.chat.interfaces.repository.UserRepository;
import com.chat.model.message.RabbitUserEntity;
import com.chat.redis.RedisStorageManager;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.messaging.handler.annotation.Header;
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

    @RabbitListener(queues = "${spring.rabbitmq.template.default-receive-queue}")
    public void consumerUserRequests(String username,
                                     @Header(AmqpHeaders.REPLY_TO) String replayTo,
                                     @Header(AmqpHeaders.CORRELATION_ID) String correlationId) {
        RabbitUserEntity user = (RabbitUserEntity) redisStorageManager.map.get("users", username);
        if (user != null) {
            this.rabbitTemplate.convertAndSend(rabbitProperties.getTemplate().getExchange(), replayTo, user);
        } else {
            this.userRepository.findByUsername(username)
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
//                        redisStorageManager.map.put("users", username, userEntity);
                    }).doOnNext(userEntity -> {
                        var corrData = new CorrelationData();
                        corrData.setId(correlationId);
                        this.rabbitTemplate.convertAndSend(rabbitProperties.getTemplate().getExchange(), replayTo, userEntity, corrData);
                    })
                    .doOnError(error -> {
                        logger.error("Error while responding request from auth service: %s", error);
                    })
                    .doOnSuccess(rabbitUserEntity -> {
                        logger.info("Response sent successfully for username: ${}", rabbitUserEntity.getUsername());
                    })
                    .subscribe();
        }
    }
}
