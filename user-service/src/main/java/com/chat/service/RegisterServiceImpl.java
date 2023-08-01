package com.chat.service;

import com.chat.config.UserLogger;
import com.chat.constants.UserStatus;
import com.chat.exception.CustomException;
import com.chat.interfaces.repository.UserRepository;
import com.chat.interfaces.service.RegisterService;
import com.chat.model.entity.UserEntity;
import com.chat.model.request.RegisterContextRequest;
import com.chat.model.request.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import static com.chat.constant.ErrorConstant.ErrorMessage.USERNAME_IN_USE;
import static com.chat.constant.SuccessConstant.FAILED;
import static com.chat.constant.SuccessConstant.SUCCESS;

@Service
@RequiredArgsConstructor
public class RegisterServiceImpl implements RegisterService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserLogger LOG;
    private final UserElasticService userElasticService;

    @Override
    @Transactional
    public Mono<String> register(Mono<RegisterRequest> registerRequestMono) {
        return registerRequestMono
                .doOnNext(body -> {
                    LOG.register.info(String.format("User register request with payload: %s", body.toString()));
                })
                .map(RegisterContextRequest::new)
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
                                .build())
                        .map(registerContextRequest::setNewUser)
                        .thenReturn(registerContextRequest))
                .map(RegisterContextRequest::getNewUser)
                .flatMap(this.userElasticService::send).map(senderResult -> SUCCESS)
                .onErrorResume(Mono::error)
                .doOnError(ex ->
                        LOG.register.info("User register failed with ex: {}", ex.getMessage(), ex)
                )
                .switchIfEmpty(Mono.just(FAILED))
                .doOnSuccess(s -> LOG.register.info("User register done with message: {}", s))
                .subscribeOn(Schedulers.boundedElastic());
    }
}
