package com.chat.interfaces.controller;

import com.chat.interfaces.service.RegisterService;
import com.chat.model.request.RegisterRequest;
import org.springframework.web.bind.annotation.PostMapping;
import reactor.core.publisher.Mono;

public interface RegisterController extends ParentController<RegisterService> {
    @PostMapping("/register")
    Mono<String> register(Mono<RegisterRequest> registerRequest);
}
