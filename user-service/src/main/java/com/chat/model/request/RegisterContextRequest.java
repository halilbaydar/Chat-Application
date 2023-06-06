package com.chat.model.request;

import com.chat.model.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterContextRequest {
    private RegisterRequest registerRequest;
    private Boolean existsByUsername;
    private UserEntity newUser;

    public RegisterContextRequest(RegisterRequest registerRequest) {
        this.registerRequest = registerRequest;
    }

    public UserEntity setNewUser(UserEntity userEntity) {
        this.newUser = userEntity;
        return userEntity;
    }
}
