package com.chat.controller;

import com.chat.interfaces.controller.ChatSocketController;
import com.chat.interfaces.service.ChatSocketService;
import com.chat.model.request.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatSocketControllerImpl implements ChatSocketController {
    private final ChatSocketService chatSocketService;

    @Override
    public ChatSocketService getService() {
        return chatSocketService;
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
