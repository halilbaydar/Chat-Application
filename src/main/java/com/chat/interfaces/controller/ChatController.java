package com.chat.interfaces.controller;

import com.chat.interfaces.service.ChatService;
import com.chat.model.request.CreateChatRoomRequest;
import com.chat.model.request.GetMessagesRequest;
import com.chat.model.request.PageNumberRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@RequestMapping(path = "/v1")
public interface ChatController extends ParentController<ChatService> {

    @PostMapping("/user/chat/room")
    <Res> Res createChatRoom(@Valid @ModelAttribute CreateChatRoomRequest createChatRoomRequest);

    @GetMapping("/user/chats")
    List<?> getChatsByPagination(@Valid @ModelAttribute PageNumberRequest pageNumberRequest);

    @GetMapping("/user/chat/messages")
    List<?> getChatMessagesByPagination(@Valid @ModelAttribute GetMessagesRequest getMessagesRequest);
}
