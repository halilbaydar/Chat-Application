package com.chat.model.dto;

import java.time.LocalDateTime;

public interface UserResponse {

    Long getId();

    LocalDateTime getCreatedAt();

    String getUsername();

    String getName();
}
