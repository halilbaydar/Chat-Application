package com.chat.security;

import com.chat.model.request.IdRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

public abstract class ChatPermissionEvaluator implements PermissionEvaluator {

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {

        if (targetDomainObject == null) {
            return false;
        } else if (targetDomainObject instanceof IdRequest) {
            return preAuthorize(authentication, ((IdRequest<?>) targetDomainObject).getId(), (String) permission);
        } else if (targetDomainObject instanceof ResponseEntity) {
            var responseBody = ((ResponseEntity<?>) targetDomainObject).getBody();
            Objects.requireNonNull(responseBody);
            return postAuthorize(authentication, responseBody, permission);
        }

        return false;
    }

    public abstract boolean postAuthorize(Authentication authentication, Object responseBody, Object permission);

    private boolean preAuthorize(final Authentication authentication, final Object id, final String permission) {
        var userPermissions = ((User) authentication.getPrincipal()).getAuthorities();
        return hasPermission(permission, userPermissions);
    }

    boolean hasPermission(final String requiredPermission, final Collection<GrantedAuthority> userPermissions) {
        return userPermissions.stream().map(GrantedAuthority::getAuthority).anyMatch(requiredPermission::equals);
    }

    @Override
    public boolean hasPermission(final Authentication authentication, final Serializable targetId, final String targetType, final Object permission) {
        if (targetId == null) {
            return false;
        }
        return preAuthorize(authentication, targetId, (String) permission);
    }
}
