package com.chat.model;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

public enum Role {
    USER;

    public List<SimpleGrantedAuthority> getGrantedAuthorities() {
        List<SimpleGrantedAuthority> permissions = new ArrayList<>();
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }
}
