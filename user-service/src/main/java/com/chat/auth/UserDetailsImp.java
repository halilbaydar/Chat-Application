package com.chat.auth;

import com.chat.model.entity.UserPermission;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class UserDetailsImp implements UserDetails {

    private final List<? extends GrantedAuthority> grantedAuthorities;
    private final String username;
    private final boolean isAccountNonExpired;
    private final boolean isAccountNonLocked;
    private final boolean isCredentialsNonExpired;
    private final boolean isEnabled;
    private final String password;
    private Map<String,UserPermission> permissions;
    public UserDetailsImp(
            final List<? extends GrantedAuthority> grantedAuthorities,
            final String username,
            final String password,
            final boolean isAccountNonExpired,
            final boolean isAccountNonLocked,
            final boolean isCredentialsNonExpired,
            final boolean isEnabled
    ) {
        this.grantedAuthorities = grantedAuthorities;
        this.username = username;
        this.password = password;
        this.isAccountNonExpired = isAccountNonExpired;
        this.isAccountNonLocked = isAccountNonLocked;
        this.isCredentialsNonExpired = isCredentialsNonExpired;
        this.isEnabled = isEnabled;
    }

    public Map<String, UserPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Map<String, UserPermission> permissions) {
        this.permissions = permissions;
    }

    @Override
    public final Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public final String getPassword() {
        return password;
    }

    @Override
    public final String getUsername() {
        return username;
    }

    @Override
    public final boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public final boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public final boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public final boolean isEnabled() {
        return isEnabled;
    }
}
