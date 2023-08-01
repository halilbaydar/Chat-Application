package com.chat.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

@Getter
@Setter
public class RoleDto implements GrantedAuthority {
    private String role;

    private Set<PermissionDto> permissions;

    @Override
    public String getAuthority() {
        return this.role.startsWith("ROLE_") ? this.role : "ROLE_" + this.role;
    }
}
