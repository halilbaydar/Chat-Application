package com.chat.interfaces.service;

import com.chat.model.request.CreateChatRoomRequest;
import com.chat.model.request.GetMessagesRequest;
import com.chat.model.request.PageNumberRequest;

import java.util.List;

public interface ChatService {
    List<?> getChatsByPagination(Integer pageNumber);

    List<?> getChatMessagesByPagination(GetMessagesRequest getMessagesRequest);

    <Res> Res createChatRoom(CreateChatRoomRequest createChatRoomRequest);
}
