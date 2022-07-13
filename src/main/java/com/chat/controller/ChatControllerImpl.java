package com.chat.controller;

import com.chat.interfaces.controller.ChatController;
import com.chat.interfaces.service.ChatService;
import com.chat.model.request.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatControllerImpl implements ChatController {
    private final ChatService chatService;

    @Override
    public ChatService getService() {
        return chatService;
    }

    @Override
    public void sendMessage(MessageRequest messageRequest) {
        getService().sendMessage(messageRequest);
    }

    @Override
    public void seenMessage(SeenRequest seenRequest) {
        getService().seenMessage(seenRequest);
    }

    @Override
    public void deliverMessage(DeliverRequest deliverRequest) {
        getService().deliverMessage(deliverRequest);
    }

    @Override
    public void typing(TypingRequest typingRequest) {
        getService().typing(typingRequest);
    }

    @Override
    public void online(OnlineRequest onlineRequest) {
        getService().online(onlineRequest);
    }

    @Override
    public String handleException(Throwable exception) {
        LoggerFactory.getLogger(this.getClass()).info("Chat Exception : " + exception.getMessage());
        return exception.getMessage();
    }
}
