package com.chat.filter;

import com.chat.auth.UserDetailsImp;
import com.chat.model.entity.UserResponse;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class UserServicePermissionEvaluator extends ChatPermissionEvaluator {
    @Override
    public boolean postAuthorize(Authentication authentication, Object responseBody, Object permission) {
        if (responseBody instanceof UserResponse) {
            var userPermission = ((UserDetailsImp) authentication.getPrincipal())
                    .getPermissions()
                    .get(((UserResponse) responseBody).getId());
            return hasPermission((String) permission, userPermission);
        }
        return true;
    }
}
