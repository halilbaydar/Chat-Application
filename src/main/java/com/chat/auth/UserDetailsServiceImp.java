package com.chat.auth;

import com.chat.interfaces.repository.UserRepository;
import com.chat.model.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

import static com.chat.constant.ErrorConstant.USER_NOT_EXIST;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImp implements UserDetailsService, Serializable {
    private final UserRepository userRepository;

    @Override
    public final UserDetails loadUserByUsername(final String username)
            throws UsernameNotFoundException {

        UserEntity attemptedUser = userRepository.findByUsername(username);

        if (attemptedUser == null) {
            throw new RuntimeException(USER_NOT_EXIST);
        }

        if (!attemptedUser.getUsername().equals(username)) {
            throw new RuntimeException(USER_NOT_EXIST);
        }

        List<SimpleGrantedAuthority> authorities = attemptedUser.getRole().getGrantedAuthorities();

        return new UserDetailsImp(
                authorities,
                attemptedUser.getUsername(),
                attemptedUser.getPassword(),
                true, true,
                true, true) {
        };
    }
}

