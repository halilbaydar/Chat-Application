package com.chat.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private static final long serialVersionUID = 6158443734275883814L;
    private final Object status;

    public CustomException(String message, Object status) {
        super(message);
        this.status = status;
    }

    public CustomException(String message) {
        super(message);
        this.status = null;
    }

    public CustomException(Object err) {
        super(err.toString());
        this.status = null;
    }
}
