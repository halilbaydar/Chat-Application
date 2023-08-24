package com.chat.model.entity;

import com.chat.model.Role;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.math.BigInteger;

@Getter
@Setter
@SuperBuilder
@Table(name = "chat_users")
public class UserEntity extends BaseEntity<Long, UserEntity> implements Serializable {

    @Column
    private String username;

    @Column
    private String name;

    @Column
    private String password;

    @Column
    private Role role;
}
