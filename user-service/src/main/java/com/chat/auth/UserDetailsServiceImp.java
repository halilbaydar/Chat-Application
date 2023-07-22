package com.chat.auth;

import com.chat.interfaces.repository.UserRepository;
import com.chat.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.Serializable;

import static com.chat.constant.ErrorConstant.ErrorMessage.USER_NOT_EXIST;


@Service
@RequiredArgsConstructor
public class UserDetailsServiceImp implements UserDetailsService, Serializable {
    private final UserRepository userRepository;

    @Override
    public final UserDetails loadUserByUsername(final String username)
            throws UsernameNotFoundException {
        return this.userRepository
                .findByUsername(username)
                .switchIfEmpty(Mono.error(new RuntimeException(USER_NOT_EXIST)))
                .filter(userEntity -> userEntity.getUsername().equals(username))
                .switchIfEmpty(Mono.error(new RuntimeException(USER_NOT_EXIST)))
                .map(userEntity -> {
                    return new UserDetailsImp(
                            Role.valueOf(userEntity.getRole()).getGrantedAuthorities(),
                            userEntity.getUsername(),
                            userEntity.getPassword(),
                            true, true,
                            true, true) {
                    };
                }).block();
    }
}

