package com.chat.controller;

import com.chat.interfaces.controller.ChatController;
import com.chat.interfaces.service.ChatService;
import com.chat.model.request.CreateChatRoomRequest;
import com.chat.model.request.GetMessagesRequest;
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
    public <Res> Res createChatRoom(CreateChatRoomRequest createChatRoomRequest) {
        return getService().createChatRoom(createChatRoomRequest);
    }

    @Override
    public List<?> getChatsByPagination(Integer pageNumber) {
        return getService().getChatsByPagination(pageNumber);
    }

    @Override
    public List<?> getChatMessagesByPagination(GetMessagesRequest getMessagesRequest) {
        return getService().getChatMessagesByPagination(getMessagesRequest);
    }
}
