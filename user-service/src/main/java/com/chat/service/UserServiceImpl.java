package com.chat.service;

import com.chat.interfaces.repository.UserRepository;
import com.chat.interfaces.service.UserService;
import com.chat.model.view.UserView;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    //    private final RoutingKeyUsersConsumer routingKeyUsersConsumer;

    @Override
    public Mono<UserView> getUser() {
        return ReactiveSecurityContextHolder.getContext().flatMap(securityContext ->
                userRepository.findByUsername((String) securityContext.getAuthentication().getPrincipal()));
    }

    @Override
    public Flux<UserView> getUsers() {
        return userRepository.findAllByCreatedAtLessThanEqual(LocalDateTime.now());
    }

    @Override
    public Mono<UserDetails> loadUserDetails(String username) {
        return null;
    }
}
