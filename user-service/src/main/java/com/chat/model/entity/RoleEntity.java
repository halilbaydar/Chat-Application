package com.chat.model.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

@Getter
@Setter
@SuperBuilder
@Table(name = "CHAT_ROLES")
public class RoleEntity extends BaseEntity<Long, RoleEntity> implements GrantedAuthority {
    @NotNull
    @Column("ROLE")
    private String role;

    @Transient
    private Set<Permission> permissions;

    @Override
    public String getAuthority() {
        return this.role.startsWith("ROLE_") ? this.role : "ROLE_" + this.role;
    }
}
