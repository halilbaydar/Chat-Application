package com.chat.interfaces.service;

import com.chat.model.dto.UserResponse;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<UserResponse> getUser();

    Flux<UserResponse> getUsers();

    Mono<UserDetails> loadUserDetails(String username);
}
