package com.chat.auth;

import com.chat.model.UserEntity;
import com.chat.redis.RedisStorageManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

import static com.chat.constant.ErrorConstant.ErrorMessage.USER_NOT_EXIST;
import static com.chat.constant.RedisKeyConstant.USERS;
import static com.chat.util.SHA256Utils.toSHA512;


@Service
@RequiredArgsConstructor
public class UserDetailsServiceImp implements UserDetailsService, Serializable {
    private final RedisStorageManager redisStorageManager;

    @Override
    public final UserDetails loadUserByUsername(final String username)
            throws UsernameNotFoundException {

        UserEntity attemptedUser = (UserEntity) redisStorageManager.map.get(USERS, toSHA512(username));

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

