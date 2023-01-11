package com.chat.controller;

import com.chat.interfaces.controller.UserController;
import com.chat.interfaces.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {
    private final UserService userService;

    @Override
    public <R> R getUser() {
        return this.userService.getUser();
    }

    @Override
    public <R> R getUsers() {
        return this.userService.getUsers();
    }
}
