package com.chat.model.entity;

import com.chat.constant.MessageStatus;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageEntity implements Serializable {
    @Field
    private String id;

    @Field
    @NotBlank
    private String chatId;

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
