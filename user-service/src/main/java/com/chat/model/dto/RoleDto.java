package com.chat.model.dto;

import java.util.Set;

public interface RoleDto {
    String role();

    default String getAuthority() {
        return "ROLE_" + role();
    }

    Set<PermissionDto> getPermissions();
}
