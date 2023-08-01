package com.chat.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserDto {
    private String username;
    private Set<RoleDto> roles;
    private String status;
    private String password;
}
