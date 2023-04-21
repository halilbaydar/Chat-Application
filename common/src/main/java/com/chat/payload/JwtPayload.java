package com.chat.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class JwtPayload {
    private boolean available;
    private String username;
    private List<Map<String, String>> authorities;
}
