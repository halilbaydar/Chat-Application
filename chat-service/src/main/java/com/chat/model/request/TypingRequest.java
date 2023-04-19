package com.chat.model.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class TypingRequest extends ParentMessageRequest {
    private String receiverName;
}
