package com.chat.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Date;

@Getter
@Setter
@Table(name = "users")
public class UserEntity {
    @Id
    private String id;

    @Column
    @CreatedDate
    private Date createdDate;

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
