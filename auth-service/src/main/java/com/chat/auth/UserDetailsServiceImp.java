package com.chat.auth;

import com.chat.model.dto.RoleDto;
import com.chat.model.dto.UserDto;
import com.chat.service.RabbitUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.chat.constant.ErrorConstant.ErrorMessage.USER_NOT_EXIST;


@Service
@RequiredArgsConstructor
public class UserDetailsServiceImp implements UserDetailsService, Serializable {
    private final RabbitUserService rabbitUserService;

    @Override
    public final UserDetails loadUserByUsername(final String username)
            throws UsernameNotFoundException {

        UserDto attemptedUser = this.rabbitUserService.getUserFromUserServiceByUsername(username);

        if (attemptedUser == null) {
            throw new RuntimeException(USER_NOT_EXIST);
        }

        if (!attemptedUser.getUsername().equals(username)) {
            throw new RuntimeException(USER_NOT_EXIST);
        }

        var roles = attemptedUser.getRoles().stream();
        var permissions = roles.map(RoleDto::getPermissions).flatMap(Collection::stream);

        List<SimpleGrantedAuthority> authorities = Stream.concat(roles, permissions)
                .map(GrantedAuthority::getAuthority)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new User(attemptedUser.getUsername(), attemptedUser.getPassword(), authorities);
    }
}

