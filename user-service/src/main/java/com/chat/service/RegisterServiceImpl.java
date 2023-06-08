package com.chat.service;

import com.chat.config.KafkaConfigData;
import com.chat.constants.UserStatus;
import com.chat.exception.CustomException;
import com.chat.interfaces.repository.UserRepository;
import com.chat.interfaces.service.RegisterService;
import com.chat.kafka.avro.model.UserAvroModel;
import com.chat.model.Role;
import com.chat.model.entity.UserEntity;
import com.chat.model.request.RegisterContextRequest;
import com.chat.model.request.RegisterRequest;
import com.chat.redis.RedisStorageManager;
import org.redisson.api.RedissonReactiveClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Date;

import static com.chat.constant.ErrorConstant.ErrorMessage.USERNAME_IN_USE;
import static com.chat.constant.RedisKeyConstant.USERS;
import static com.chat.constant.SuccessConstant.FAILED;
import static com.chat.constant.SuccessConstant.SUCCESS;

@Service
public class RegisterServiceImpl implements RegisterService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ReactiveKafkaProducerTemplate<String, UserAvroModel> reactiveKafkaProducerTemplate;
    private final KafkaConfigData kafkaConfigData;

    public RegisterServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
                               @Qualifier("user-to-elastic-producer-template")
                               ReactiveKafkaProducerTemplate<String, UserAvroModel> reactiveKafkaProducerTemplate,
                               KafkaConfigData kafkaConfigData) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.reactiveKafkaProducerTemplate = reactiveKafkaProducerTemplate;
        this.kafkaConfigData = kafkaConfigData;
    }

    @Override
    @Transactional
    public Mono<String> register(Mono<RegisterRequest> registerRequestMono) {
        return registerRequestMono.map(RegisterContextRequest::new)
                .flatMap(registerContextRequest -> userRepository
                        .existsByUsername(registerContextRequest.getRegisterRequest().getUsername())
                        .doOnNext(registerContextRequest::setExistsByUsername)
                        .thenReturn(registerContextRequest))
                .filter(registerContextRequest -> !registerContextRequest.getExistsByUsername())
                .switchIfEmpty(Mono.error(new CustomException(USERNAME_IN_USE, HttpStatus.BAD_REQUEST)))
                .flatMap(registerContextRequest -> this.userRepository.save(UserEntity.builder()
                                .username(registerContextRequest.getRegisterRequest().getUsername())
                                .name(registerContextRequest.getRegisterRequest().getName())
                                .password(passwordEncoder.encode(registerContextRequest.getRegisterRequest().getPassword()))
                                .status(UserStatus.ACTIVE.name())
                                .role(Role.USER.name())
                                .build())
                        .map(registerContextRequest::setNewUser)
                        .thenReturn(registerContextRequest))
                .map(RegisterContextRequest::getNewUser)
                .flatMap(userEntity -> this.reactiveKafkaProducerTemplate.send(
                        kafkaConfigData.getTopicName(),
                        userEntity.getId().toString(),
                        UserAvroModel.newBuilder()
                                .setUsername(userEntity.getUsername())
                                .setName(userEntity.getName())
                                .setCreatedDate(new Date().getTime())
                                .setId(userEntity.getId().toString())
                                .build())).map(senderResult -> SUCCESS)
                .onErrorResume(Mono::error)
                .switchIfEmpty(Mono.just(FAILED))
                .subscribeOn(Schedulers.boundedElastic());
    }
}
