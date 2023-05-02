package com.chat.service;

import com.chat.aut.Role;
import com.chat.exception.CustomException;
import com.chat.interfaces.repository.UserRepository;
import com.chat.interfaces.service.RegisterService;
import com.chat.kafka.avro.model.UserAvroModel;
import com.chat.model.entity.UserEntity;
import com.chat.model.request.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.chat.constant.ErrorConstant.ErrorMessage.USERNAME_IN_USE;
import static com.chat.constant.SuccessConstant.SUCCESS;

@Service
@RequiredArgsConstructor
public class RegisterServiceImpl implements RegisterService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final KafkaProducer<String, UserAvroModel> kafkaProducer;

    @Value("${topic::user}")
    private String userTopic;

    @Override
    @Transactional
    public <R> R register(RegisterRequest registerRequest) {
        boolean existsByUsername = userRepository.existsByUsername(registerRequest.getUsername());
        if (existsByUsername) {
            throw new CustomException(USERNAME_IN_USE, HttpStatus.BAD_REQUEST);
        }
        UserEntity newUser = new UserEntity();
        newUser.setUsername(registerRequest.getUsername());

        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
        newUser.setPassword(encodedPassword);
        newUser.setRole(Role.USER);
        UserEntity userEntity = userRepository.save(newUser);

        kafkaProducer.send(userTopic, null, UserAvroModel.newBuilder()
                .setUsername(userEntity.getUsername())
                .setName(userEntity.getName())
                .setCreatedDate(userEntity.getCreatedDate().getTime())
                .setId(userEntity.getId())
                .build());
        return (R) SUCCESS;
    }
}
