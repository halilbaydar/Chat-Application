package com.chat.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageRequest extends ParentMessageRequest {
    private String message;
    private String recipientId;
    private String senderId;
    private String id;
}
