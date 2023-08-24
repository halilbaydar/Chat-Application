package com.chat.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class UserModel {
    public Long id;
    public Object createdAt;
    public Object updatedAt;
    public String username;
    public String name;
    public String password;
    public String role;
}
