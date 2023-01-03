package com.chat.model.entity;

import com.chat.model.common.ParentEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

@Getter
@Setter
@Document(value = "chats")
public class ChatEntity extends ParentEntity {
    @Field
    private Stack<MessageEntity> messages = new Stack<>();

    @Indexed
    private Set<String> users = new HashSet<>();

    @Field
    private ChatType type;

    public enum ChatType {
        SINGLE, GROUP
    }
}
