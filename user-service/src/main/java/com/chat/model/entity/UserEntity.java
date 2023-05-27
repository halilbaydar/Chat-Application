package com.chat.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

@Getter
@Setter
@Table(name = "users")
public class UserEntity implements Serializable {
    @Id
    private BigInteger id;

    @Column
    @CreatedDate
    private Date createdAt;

    @Column
    private Date deletedAt;

    @Column
    @LastModifiedDate
    private Date updatedAt;

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
