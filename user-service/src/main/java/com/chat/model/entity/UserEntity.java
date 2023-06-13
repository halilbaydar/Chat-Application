package com.chat.model.entity;

import lombok.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.math.BigInteger;

@Getter
@Setter
@Builder
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity extends AbstractEntity<BigInteger> implements Serializable {

    @Column
    private String username;

    @Column
    private String name;

    @Column
    private String password;

    @Column
    private String role;

    @Column
    private String status;
}
