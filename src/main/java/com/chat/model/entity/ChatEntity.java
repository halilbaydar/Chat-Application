package com.chat.model.entity;

import com.chat.model.common.ParentEntity;
import com.chat.model.common.Role;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

@Getter
@Setter
@Document(value = "users")
public class ChatEntity extends ParentEntity {
    @Field
    private Stack<MessageEntity> messages = new Stack<>();

    @Indexed
    @DBRef
    private Set<UserEntity> users = new HashSet<>(2);
}
