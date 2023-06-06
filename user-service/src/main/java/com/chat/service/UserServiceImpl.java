package com.chat.service;

import com.chat.constants.UserStatus;
//import com.chat.consumer.RoutingKeyUsersConsumer;
import com.chat.interfaces.repository.UserRepository;
import com.chat.interfaces.service.UserService;
import com.chat.model.entity.UserResponse;
//import com.chat.util.SessionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
//    private final RoutingKeyUsersConsumer routingKeyUsersConsumer;

    @Override
    public Mono<UserResponse> getUser() {
//        UserResponse activeUser = routingKeyUsersConsumer.consume()
        return Mono.just(null);
    }

    @Override
    public Flux<UserResponse> getUsers() {
        return userRepository.findAllByStatus(UserStatus.ACTIVE.name());
    }
}
