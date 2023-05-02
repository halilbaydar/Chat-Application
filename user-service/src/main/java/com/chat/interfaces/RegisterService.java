package com.chat.interfaces;

import com.chat.model.request.RegisterRequest;

public interface RegisterService {
    <R> R register(RegisterRequest registerRequest);
}
