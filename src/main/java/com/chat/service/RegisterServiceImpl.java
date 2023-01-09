package com.chat.service;

import com.chat.interfaces.repository.UserRepository;
import com.chat.interfaces.service.RegisterService;
import com.chat.model.common.Role;
import com.chat.model.entity.UserEntity;
import com.chat.model.request.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterServiceImpl implements RegisterService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //@Transactional
    @Override
    public <R> R register(RegisterRequest registerRequest) {
        UserEntity user = userRepository.findByUsername(registerRequest.getUsername());
        if (user != null) {
            throw new RuntimeException("USERNAME IN USE");
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
