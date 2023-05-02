package com.chat.interfaces;

import com.chat.model.request.RegisterRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface RegisterController extends ParentController<RegisterService> {
    @PostMapping("v1/register")
    <R> R register(@RequestBody() RegisterRequest registerRequest);
}
