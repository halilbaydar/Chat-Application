package com.chat.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ApiError {
    private int status;
    private String message;
    private String path;

    private Map<String, String> errors;

    public ApiError(int status, String message, String path) {
        this.message = message;
        this.status = status;
        this.path = path;
    }
}
