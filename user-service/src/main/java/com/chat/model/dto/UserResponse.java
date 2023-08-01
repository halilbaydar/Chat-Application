package com.chat.model.dto;

import com.chat.model.entity.UserEntity;

import java.math.BigInteger;
import java.time.LocalDateTime;

public interface UserResponse {
    static UserResponse of(UserEntity userEntity) {
        return new UserResponse() {
            @Override
            public BigInteger getId() {
                return userEntity.getId();
            }

            @Override
            public LocalDateTime getCreatedAt() {
                return userEntity.getCreatedAt();
            }

            @Override
            public String getUsername() {
                return userEntity.getUsername();
            }

            @Override
            public String getName() {
                return userEntity.getName();
            }
        };
    }

    BigInteger getId();

    LocalDateTime getCreatedAt();

    String getUsername();

    String getName();
}
