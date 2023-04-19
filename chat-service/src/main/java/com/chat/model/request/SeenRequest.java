package com.chat.model.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class SeenRequest extends ParentMessageRequest {
    private String recipientId;
    private String messageId;
}
