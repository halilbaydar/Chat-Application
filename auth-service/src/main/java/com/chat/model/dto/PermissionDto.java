package com.chat.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Getter
@Setter
public class PermissionDto implements GrantedAuthority {
    private String authority;
}
