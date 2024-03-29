package com.chat.controller;

import com.chat.interfaces.controller.UserController;
import com.chat.interfaces.service.UserService;
import com.chat.model.view.UserView;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {
    private final UserService userService;

    @Override
    public Mono<UserView> getUser() {
        return this.userService.getUser();
    }

    @Override
    public Flux<UserView> getUsers() {
        return this.userService.getUsers();
    }
}
