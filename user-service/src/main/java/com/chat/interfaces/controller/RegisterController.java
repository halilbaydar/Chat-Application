package com.chat.interfaces.controller;

import com.chat.interfaces.service.RegisterService;
import com.chat.model.request.RegisterRequest;

public interface RegisterController extends ParentController<RegisterService> {
    <R> R register(RegisterRequest registerRequest);
}
