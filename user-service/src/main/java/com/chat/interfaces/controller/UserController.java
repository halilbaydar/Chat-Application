package com.chat.interfaces.controller;

import com.chat.model.entity.UserResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequestMapping(path = "/v1")
public interface UserController {

    @GetMapping("/user")
    Mono<UserResponse> getUser();

    @GetMapping("/user/users")
    Flux<UserResponse> getUsers();
}
