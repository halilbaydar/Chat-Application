package com.chat.model.entity;

import com.chat.aut.Role;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity(name = "users")
@Table(name = "users", indexes = {
        @Index(name = "username", columnList = "username", unique = true)
})
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
    private Role role;
}
