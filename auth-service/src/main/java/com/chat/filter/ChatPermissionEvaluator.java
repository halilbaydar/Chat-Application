package com.chat.filter;

import com.chat.auth.UserDetailsImp;
import com.chat.model.request.IdRequest;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;


@Component
public class ChatPermissionEvaluator implements PermissionEvaluator {

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {

        if (targetDomainObject == null) {
            return false;
        }

        if (targetDomainObject instanceof IdRequest) {
            return checkPermission(authentication, ((IdRequest) targetDomainObject).getId(), permission);
        }

        return false;
    }

    private boolean checkPermission(Authentication authentication, Object id, Object permission) {
        Collection<? extends GrantedAuthority> permissions = ((UserDetailsImp) authentication.getPrincipal()).getPermissions();
        if (permissions == null || permissions.size() == 0) {
            return true;
        }
        return permissions.stream().anyMatch(userPer -> hasPermission((String) permission, userPer.getAuthority()));
    }

    private boolean hasPermission(final String requiredPermission, final String userPermission) {
        return requiredPermission.equals(userPermission);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }
}
