package com.chat.interfaces.service;

import com.chat.model.request.PageNumberRequest;

import java.util.List;

public interface ChatService {
    List<?> getChatsByPagination(PageNumberRequest pageNumberRequest);

    List<?> getChatMessagesByPagination(PageNumberRequest pageNumberRequest);
}
