package com.chat.service;

import com.chat.config.KafkaConfigData;
import com.chat.exception.CustomException;
import com.chat.interfaces.repository.UserRepository;
import com.chat.interfaces.service.RegisterService;
import com.chat.kafka.avro.model.UserAvroModel;
import com.chat.model.Role;
import com.chat.model.entity.UserEntity;
import com.chat.model.request.RegisterContextRequest;
import com.chat.model.request.RegisterRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import static com.chat.constant.ErrorConstant.ErrorMessage.USERNAME_IN_USE;
import static com.chat.constant.RedisKeyConstant.USERS;
import static com.chat.constant.SuccessConstant.SUCCESS;
import static com.chat.util.SHA256Utils.toSHA512;

@Service
public class RegisterServiceImpl implements RegisterService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ReactiveKafkaProducerTemplate<String, UserAvroModel> reactiveKafkaProducerTemplate;
    private final KafkaConfigData kafkaConfigData;

    public RegisterServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
                               @Qualifier("user-to-elastic-producer-template")
                               ReactiveKafkaProducerTemplate<String, UserAvroModel> reactiveKafkaProducerTemplate, KafkaConfigData kafkaConfigData) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.reactiveKafkaProducerTemplate = reactiveKafkaProducerTemplate;
        this.kafkaConfigData = kafkaConfigData;
    }
    //    private final RedisStorageManager redisStorageManager;

//    @Value("${topic.user}")
//    private String userTopic;

    @Override
    @Transactional
    public Mono<String> register(Mono<RegisterRequest> registerRequestMono) {
        return registerRequestMono.map(RegisterContextRequest::new).flatMap(registerContextRequest -> {
            Mono<Boolean> result = userRepository.existsByUsername(registerContextRequest.getRegisterRequest().getUsername());
            return result.doOnNext(registerContextRequest::setExistsByUsername).thenReturn(registerContextRequest);
        }).flatMap(registerContextRequest -> {
            if (!registerContextRequest.getExistsByUsername()) {
                UserEntity newUser = new UserEntity();
                newUser.setUsername(registerContextRequest.getRegisterRequest().getUsername());
                String encodedPassword = passwordEncoder.encode(registerContextRequest.getRegisterRequest().getPassword());
                newUser.setPassword(encodedPassword);
                newUser.setRole(Role.USER.name());
                registerContextRequest.setNewUser(newUser);
                return Mono.just(registerContextRequest);
            } else {
                return Mono.error(new CustomException(USERNAME_IN_USE, HttpStatus.BAD_REQUEST));
            }
        }).flatMap(registerContextRequest -> {
            UserEntity userEntity = registerContextRequest.getNewUser();
            return this.reactiveKafkaProducerTemplate.send(kafkaConfigData.getTopicName(), userEntity.getId(), UserAvroModel.newBuilder()
                    .setUsername(userEntity.getUsername())
                    .setName(userEntity.getName())
                    .setCreatedDate(userEntity.getCreatedDate().getTime())
                    .setId(userEntity.getId())
                    .build());
//            redisStorageManager.map.put(USERS, toSHA512(userEntity.getUsername()), userEntity);
        }).map(senderResult -> SUCCESS).onErrorResume(Mono::error).subscribeOn(Schedulers.boundedElastic());
    }
}
