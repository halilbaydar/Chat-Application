package com.chat.interfaces.service;

import com.chat.model.request.RegisterRequest;

public interface RegisterService {
    <R> R register(RegisterRequest registerRequest);
}
