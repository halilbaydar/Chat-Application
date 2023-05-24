package com.chat.service;
//
//import com.chat.aut.Role;
//import com.chat.exception.CustomException;
//import com.chat.interfaces.repository.UserRepository;
//import com.chat.interfaces.service.RegisterService;
//import com.chat.kafka.avro.model.UserAvroModel;
//import com.chat.model.entity.UserEntity;
//import com.chat.model.request.RegisterContextRequest;
//import com.chat.model.request.RegisterRequest;
//import com.chat.redis.RedisStorageManager;
//import lombok.RequiredArgsConstructor;
//import org.apache.kafka.clients.producer.KafkaProducer;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import reactor.core.publisher.Mono;
//import reactor.core.scheduler.Schedulers;
//
//import static com.chat.constant.ErrorConstant.ErrorMessage.USERNAME_IN_USE;
//import static com.chat.constant.RedisKeyConstant.USERS;
//import static com.chat.constant.SuccessConstant.SUCCESS;
//import static com.chat.util.SHA256Utils.toSHA512;
//
//@Service
//@RequiredArgsConstructor
//public class RegisterServiceImpl implements RegisterService {
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//    //    private final KafkaProducer<String, UserAvroModel> kafkaProducer;
//    private final RedisStorageManager redisStorageManager;
//
//    @Value("${topic.user}")
//    private String userTopic;
//
//    @Override
//    @Transactional
//    public Mono<String> register(Mono<RegisterRequest> registerRequestMono) {
//        return registerRequestMono.map(RegisterContextRequest::new)
//                .flatMap(registerContextRequest -> {
//                    Mono<Boolean> result = userRepository
//                            .existsByUsername(registerContextRequest.getRegisterRequest().getUsername());
//                    return result.doOnNext(registerContextRequest::setExistsByUsername)
//                            .thenReturn(registerContextRequest);
//                })
//                .flatMap(registerContextRequest -> {
//                    if (!registerContextRequest.getExistsByUsername()) {
//                        UserEntity newUser = new UserEntity();
//                        newUser.setUsername(registerContextRequest.getRegisterRequest().getUsername());
//                        String encodedPassword = passwordEncoder.encode(registerContextRequest.getRegisterRequest().getPassword());
//                        newUser.setPassword(encodedPassword);
//                        newUser.setRole(Role.USER);
//                        registerContextRequest.setNewUser(newUser);
//                        return Mono.just(registerContextRequest);
//                    } else {
//                        return Mono.error(new CustomException(USERNAME_IN_USE, HttpStatus.BAD_REQUEST));
//                    }
//                })
//                .flatMap(registerContextRequest -> {
//                    UserEntity userEntity = registerContextRequest.getNewUser();
////                    kafkaProducer.send(userTopic, userEntity.getId(), UserAvroModel.newBuilder()
////                            .setUsername(userEntity.getUsername())
////                            .setName(userEntity.getName())
////                            .setCreatedDate(userEntity.getCreatedDate().getTime())
////                            .setId(userEntity.getId())
////                            .build());
//                    redisStorageManager.map.put(USERS, toSHA512(userEntity.getUsername()), userEntity);
//                    return Mono.just(registerContextRequest);
//                }).map(registerContextRequest -> SUCCESS)
//                .onErrorResume(Mono::error)
//                .subscribeOn(Schedulers.boundedElastic());
//    }
//}
