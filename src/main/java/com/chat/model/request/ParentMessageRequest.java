package com.chat.model.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ParentMessageRequest implements Serializable {
    private String recipientId;
    private String chatId;
}
