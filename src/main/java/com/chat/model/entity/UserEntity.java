package com.chat.model.entity;

import com.chat.model.common.ParentEntity;
import com.chat.model.common.Role;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document(value = "users")
public class UserEntity extends ParentEntity {
    @Field
    private String username;

    @Field
    private Role role;
}
