package com.chat.service;

import com.chat.interfaces.service.ChatService;
import com.chat.model.request.PageNumberRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    @Override
    public List<?> getChatsByPagination(PageNumberRequest pageNumberRequest) {
        return null;
    }

    @Override
    public List<?> getChatMessagesByPagination(PageNumberRequest pageNumberRequest) {
        return null;
    }
}
