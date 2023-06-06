package com.chat.consumer;
//
//import com.chat.interfaces.repository.UserRepository;
//import com.chat.model.RabbitUserEntity;
//import com.chat.redis.RedisStorageManager;
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//import reactor.core.publisher.Mono;
//
//import java.time.Duration;
//import java.util.Date;
//import java.util.Objects;
//
//@Component
//@RequiredArgsConstructor
//public class RoutingKeyUsersConsumer implements RMessageConsumer<String, Object> {
//    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
//    private final UserRepository userRepository;
//    private final RedisStorageManager redisStorageManager;
//
//    @Override
//    @UserRequestsListener
//    public Object consume(String username) throws RuntimeException {
//        return redisStorageManager.value.get(username)
//                .switchIfEmpty(this.userRepository.findByUsername(username))
//                .filter(Objects::nonNull)
//                .switchIfEmpty(Mono.error(new RuntimeException(String.format("username is not valid %s", username))))
//                .map(user -> (((RabbitUserEntity) user)))
//                .map(userEntity -> new RabbitUserEntity(
//                        userEntity.getId(),
//                        userEntity.getUsername(),
//                        userEntity.getName(),
//                        userEntity.getRole(),
//                        userEntity.getStatus(),
//                        userEntity.getPassword(),
//                        new Date().getTime(),
//                        new Date().getTime(),
//                        new Date().getTime())
//                )
//                .flatMap(userEntity -> redisStorageManager.value
//                        .set(username, userEntity, Duration.ofSeconds(10))
//                        .thenReturn(userEntity))
//                .doOnError(error -> {
//                    logger.error("Error while responding request from auth service: %s", error);
//                })
//                .doOnSuccess(rabbitUserEntity -> {
//                    logger.info("Response sent successfully for username: ${}", rabbitUserEntity.getUsername());
//                })
//                .block();
//    }
//}
