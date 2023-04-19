package com.chat.util;

public final class StringUtil {
    public static Boolean isBlank(String token) {
        return token == null || token.length() == 0 || token.equals("null") || token.equals("undefined");
    }
}
