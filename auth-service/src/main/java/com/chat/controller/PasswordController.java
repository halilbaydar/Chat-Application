package com.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize("isAuthenticated()")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/v1/user")
public class PasswordController {

    @PreAuthorize("hasRole('RESET_PASSWORD') && hasAuthority('OPTIONS_UPDATE')")
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam("password") String password) {
        return null;
    }
}
