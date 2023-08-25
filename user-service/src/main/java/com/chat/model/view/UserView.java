package com.chat.model.view;

import java.time.LocalDateTime;

public interface UserView {

    Long getId();

    LocalDateTime getCreatedAt();

    String getUsername();

    String getName();
}
