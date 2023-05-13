package com.chat.interfaces.service;

import com.chat.model.request.RegisterRequest;
import reactor.core.publisher.Mono;

public interface RegisterService {
    Mono<String> register(Mono<RegisterRequest> registerRequest);
}
