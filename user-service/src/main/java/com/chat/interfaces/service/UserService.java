package com.chat.interfaces.service;

import com.chat.model.view.UserView;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<UserView> getUser();

    Flux<UserView> getUsers();

    Mono<UserDetails> loadUserDetails(String username);
}
