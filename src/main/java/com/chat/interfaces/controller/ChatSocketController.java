package com.chat.interfaces.controller;

import com.chat.interfaces.service.ChatService;
import com.chat.model.request.*;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;

import javax.validation.Valid;

public interface ChatController extends ParentController<ChatService> {
    @MessageMapping("/chat/message/send")
    void sendMessage(@Valid @Payload MessageRequest messageRequest);

    @MessageMapping("/chat/message/seen")
    void seenMessage(@Valid @Payload SeenRequest seenRequest);

    @MessageMapping("/chat/message/deliver")
    void deliverMessage(@Valid @Payload DeliverRequest deliverRequest);

    @MessageMapping("/chat/typing")
    void typing(@Valid @Payload TypingRequest typingRequest);

    @MessageMapping("/chat/online")
    void online(@Valid @Payload OnlineRequest onlineRequest);

    @MessageExceptionHandler
    String handleException(Throwable exception);
}
