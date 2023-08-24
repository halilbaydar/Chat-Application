package com.chat.model.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class RegisterRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String name;
    @NotBlank
    private String password;
}
