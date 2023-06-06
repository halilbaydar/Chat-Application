package com.chat.model.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Builder
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity implements Serializable {
    @Id
    private BigInteger id;

    @Column("created_at")
    @CreatedDate
    private Long createdAt;

    @Column("deleted_at")
    private Long deletedAt;

    @Column("updated_at")
    @LastModifiedDate
    private Long updatedAt;

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
