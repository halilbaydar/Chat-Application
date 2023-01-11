package com.chat.interfaces.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(path = "/v1")
public interface UserController {

    @GetMapping("/user")
    <R> R getUser();

    @GetMapping("/users")
    <R> R getUsers();
}
