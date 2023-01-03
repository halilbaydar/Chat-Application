package com.chat.model.entity;

import com.chat.constant.MessageStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@Builder
public class MessageEntity implements Serializable {
    @Field
    @NotBlank
    private String ChatId;

    @Field
    @NotBlank
    private String senderId;

    @Field
    @NotBlank
    private String recipientId;

    @Field
    @NotBlank
    private String message;

    @Field
    @NotNull
    private MessageStatus messageStatus;
}
