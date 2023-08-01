package com.chat.model.dto;

import java.util.Set;

public interface UserDto {
    String getUsername();

    String getPassword();

    Set<RoleDto> getRoles();

    String getStatus();
}
