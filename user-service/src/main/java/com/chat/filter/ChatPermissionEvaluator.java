package com.chat.filter;

import com.chat.auth.UserDetailsImp;
import com.chat.model.entity.UserPermission;
import com.chat.model.request.IdRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

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
        var firstPermission = Optional.ofNullable(
                ((UserDetailsImp) authentication.getPrincipal())
                        .getPermissions()
                        .get((String) id)
        ).orElse(UserPermission.empty());

        if (firstPermission.isEmpty()) {
            return true;
        }
        return hasPermission(permission, firstPermission);
    }

    boolean hasPermission(final String requiredPermission, final UserPermission userPermission) {
        return requiredPermission.equals(userPermission.getPermissionType());
    }

    @Override
    public boolean hasPermission(final Authentication authentication, final Serializable targetId, final String targetType, final Object permission) {
        if (targetId == null) {
            return false;
        }
        return preAuthorize(authentication, targetId, (String) permission);
    }
}
