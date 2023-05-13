package com.chat.interfaces.service;

import com.chat.model.entity.UserResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<UserResponse> getUser();

    Flux<UserResponse> getUsers();
}
