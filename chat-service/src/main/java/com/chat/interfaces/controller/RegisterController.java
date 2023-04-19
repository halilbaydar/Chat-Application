package com.chat.interfaces.controller;

import com.chat.model.request.RegisterRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface RegisterController {
    @PostMapping("v1/register")
    <R> R register(@RequestBody() RegisterRequest registerRequest);
}
