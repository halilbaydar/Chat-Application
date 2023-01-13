package com.chat.interfaces.controller;

import com.chat.interfaces.service.ChatSocketService;
import com.chat.model.request.MessageRequest;
import com.chat.model.request.OnlineRequest;
import com.chat.model.request.SeenRequest;
import com.chat.model.request.TypingRequest;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;

import javax.validation.Valid;

public interface ChatSocketController extends ParentController<ChatSocketService> {
    @MessageMapping("/chat/message/send")
    void sendMessage(@Valid @Payload MessageRequest messageRequest);

    @MessageMapping("/chat/message/seen")
    void seenMessage(@Valid @Payload SeenRequest seenRequest);

    @MessageMapping("/chat/typing")
    void typing(@Valid @Payload TypingRequest typingRequest);

    @MessageMapping("/chat/online")
    void online(@Valid @Payload OnlineRequest onlineRequest);

    @MessageExceptionHandler
    String handleException(Throwable exception);
}
