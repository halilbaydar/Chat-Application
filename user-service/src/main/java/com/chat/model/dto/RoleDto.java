package com.chat.model.dto;

import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

public interface RoleDto extends GrantedAuthority {
    String role();

    default String getAuthority() {
        return "ROLE_" + role();
    }

    Set<PermissionDto> getPermissions();
}
