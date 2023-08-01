package com.chat.service;

import com.chat.constants.UserStatus;
import com.chat.interfaces.repository.UserRepository;
import com.chat.interfaces.service.UserService;
import com.chat.model.dto.PermissionDto;
import com.chat.model.dto.RoleDto;
import com.chat.model.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
//    private final RoutingKeyUsersConsumer routingKeyUsersConsumer;

    @Override
    public Mono<UserResponse> getUser() {
        return ReactiveSecurityContextHolder.getContext()
                .flatMap(securityContext ->
                        userRepository.findByUsername((String) securityContext.getAuthentication().getPrincipal())
                ).map(UserResponse::of);
    }

    @Override
    public Flux<UserResponse> getUsers() {
        return userRepository.findAllByStatus(UserStatus.ACTIVE.name());
    }

    @Override
    public Mono<UserDetails> loadUserDetails(String username) {
        return userRepository.loadUserWithRolesByUsername(username)
                .map(userDto -> new User(userDto.getUsername(), null,
                        Stream.concat(userDto.getRoles().stream().map(RoleDto::getAuthority),
                                        userDto.getRoles().stream().map(RoleDto::getPermissions)
                                                .flatMap(Collection::stream).map(PermissionDto::getAuthority)
                                ).map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toList())));
    }
}
