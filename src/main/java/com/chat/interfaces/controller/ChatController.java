package com.chat.interfaces.controller;

import com.chat.interfaces.service.ChatService;
import com.chat.model.request.PageNumberRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.validation.Valid;
import java.util.List;

public interface ChatController extends ParentController<ChatService> {

    @GetMapping("/user/chats")
    List<?> getChatsByPagination(@Valid @ModelAttribute PageNumberRequest pageNumberRequest);

    @GetMapping("/user/chat/messages")
    List<?> getChatMessagesByPagination(@Valid @ModelAttribute PageNumberRequest pageNumberRequest);
}
