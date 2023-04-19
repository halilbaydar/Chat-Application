package com.chat.constant;

import org.springframework.http.HttpStatus;

public class ErrorConstant {
    public static class ErrorMessage {
        public static final String USER_NOT_EXIST = "0001";
        public static final String INVALID_OPERATION = "0002";
        public static final String CHAT_ALREADY_EXISTS = "0003";
        public static final String CHAT_NOT_FOUND = "0004";
        public static final String INVALID_TOKEN = "0005";
    }

    public static class ErrorStatus {
        public static final String UNAUTHORIZED = HttpStatus.UNAUTHORIZED.name();
    }
}
