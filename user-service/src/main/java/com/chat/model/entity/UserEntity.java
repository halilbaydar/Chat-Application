package com.chat.model.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Set;

@Getter
@Setter
@SuperBuilder
@Table(name = "CHAT_USERS")
public class UserEntity extends BaseEntity<BigInteger, UserEntity> implements Serializable {

    @Column("USERNAME")
    private String username;

    @Column("NAME")
    private String name;

    @Column("PASSWORD")
    private String password;

    @Column("STATUS")
    private String status;

    @Transient
    private Set<RoleEntity> roles;
}
