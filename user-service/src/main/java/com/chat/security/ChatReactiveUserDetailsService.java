package com.chat.auth;

import com.chat.interfaces.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ChatReactiveUserDetailsService implements ReactiveUserDetailsService {
    private final UserService userService;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userService.loadUserWithRolesByUsername(username);
    }
}
