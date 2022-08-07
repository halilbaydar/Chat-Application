package com.chat.controller;

import com.chat.interfaces.controller.ChatController;
import com.chat.interfaces.service.ChatService;
import com.chat.model.request.PageNumberRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatControllerImpl implements ChatController {
    private final ChatService chatService;

    @Override
    public ChatService getService() {
        return chatService;
    }

    @Override
    public List<?> getChatsByPagination(PageNumberRequest pageNumberRequest) {
        return getService().getChatsByPagination(pageNumberRequest);
    }

    @Override
    public List<?> getChatMessagesByPagination(PageNumberRequest pageNumberRequest) {
        return getService().getChatMessagesByPagination(pageNumberRequest);
    }
}
