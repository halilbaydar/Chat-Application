package com.chat.model;

import com.chat.auth.Role;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class UserEntity implements Serializable {
    private String id;

    private String username;

    private String password;

    private Role role;

    private Date createdDate;
}
