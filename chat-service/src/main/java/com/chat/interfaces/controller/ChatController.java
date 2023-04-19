package com.chat.interfaces.controller;

import com.chat.interfaces.service.ChatService;
import com.chat.model.request.CreateChatRoomRequest;
import com.chat.model.request.GetMessagesRequest;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping(path = "/v1")
public interface ChatController extends ParentController<ChatService> {

    @PostMapping("/user/chat/room")
    <Res> Res createChatRoom(@Valid @RequestBody CreateChatRoomRequest createChatRoomRequest);

    @GetMapping("/user/chats")
    List<?> getChatsByPagination(@RequestParam("pageNumber") Integer pageNumber);

    @GetMapping("/user/chat/messages")
    List<?> getChatMessagesByPagination(@Valid @RequestBody GetMessagesRequest getMessagesRequest);
}
