package com.chat.model.entity;

import com.chat.constant.MessageStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
public class MessageEntity {
    @NotBlank
    @Field
    private String ChatId;

    @NotBlank
    @Field
    private String senderId;

    @NotBlank
    @Field
    private String recipientId;

    @Field
    private String message;

    @NotNull
    @Field
    private MessageStatus messageStatus;
}
