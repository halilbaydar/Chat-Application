package com.chat.model.entity;

import com.chat.aut.Role;

import java.util.Date;

public interface User {
    Long getId();

    String getUsername();

    String getName();

    Date getCreatedDate();

    String password();

    Role getRole();
}
