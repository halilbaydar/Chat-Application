package com.chat.interfaces.controller;

import com.chat.interfaces.service.RegisterService;
import com.chat.model.request.RegisterRequest;
import org.springframework.web.bind.annotation.PostMapping;

public interface RegisterController extends ParentController<RegisterService> {
    @PostMapping("/register")
    <R> R register(RegisterRequest registerRequest);
}
