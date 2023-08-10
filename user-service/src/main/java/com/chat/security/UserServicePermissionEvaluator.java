package com.chat.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class UserServicePermissionEvaluator extends ChatPermissionEvaluator {
    @Override
    public boolean postAuthorize(Authentication authentication, Object responseBody, Object permission) {
        return true;
    }
}
