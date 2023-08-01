package com.chat.model.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;

@Getter
@Setter
@SuperBuilder
@Table(name = "CHAT_PERMISSONS")
public class Permission extends BaseEntity<Long, Permission> implements GrantedAuthority {
    @Column("AUTHORITY")
    private String authority;
}
