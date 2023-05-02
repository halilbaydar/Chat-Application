package com.chat.service;

import com.chat.aut.Role;
import com.chat.exception.CustomException;
import com.chat.interfaces.RegisterService;
import com.chat.interfaces.repository.UserRepository;
import com.chat.model.entity.User;
import com.chat.model.entity.UserEntity;
import com.chat.model.request.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.chat.constant.ErrorConstant.ErrorMessage.USERNAME_IN_USE;

@Service
@RequiredArgsConstructor
public class RegisterServiceImpl implements RegisterService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public <R> R register(RegisterRequest registerRequest) {
        User user = userRepository.findByUsername(registerRequest.getUsername());
        if (user != null) {
            throw new CustomException(USERNAME_IN_USE, HttpStatus.BAD_REQUEST);
        }
        UserEntity newUser = new UserEntity();
        newUser.setUsername(registerRequest.getUsername());

        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
        newUser.setPassword(encodedPassword);
        newUser.setRole(Role.USER);
        userRepository.save(newUser);
        return (R) "SUCCESS";
    }
}
