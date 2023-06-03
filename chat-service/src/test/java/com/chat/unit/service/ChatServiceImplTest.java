package com.chat.unit.service;

import com.chat.common.AbstractIT;
import com.chat.interfaces.repository.ChatEntityRepository;
import com.chat.interfaces.service.ChatService;
import com.chat.model.entity.ChatEntity;
import com.chat.model.request.CreateChatRoomRequest;
import com.chat.rabbit.RabbitUserService;
import com.chat.service.ChatServiceImpl;
import com.chat.util.SessionUtil;
import com.mongodb.client.MongoDatabase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(value = {MockitoExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
class ChatServiceImplTest extends AbstractIT {

    @Mock
    private ChatEntityRepository chatEntityRepository;

    @Mock
    private RabbitUserService rabbitUserService;

    @Mock
    private MongoDatabase mongoDatabase;

    private ChatService chatService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        chatService = new ChatServiceImpl(chatEntityRepository, mongoDatabase, rabbitUserService);
        Mockito.when(rabbitUserService.receiveUserFromUserService(anyString())).thenReturn(user1);
    }

    @Test
    void getChatsByPagination() {
    }

    @Test
    void getChatMessagesByPagination() {
    }


    @Test
    void createChatRoom() {
        try (MockedStatic<SessionUtil> mockedFactory = Mockito.mockStatic(SessionUtil.class)) {
            mockedFactory.when(SessionUtil::getActiveUser).thenReturn(user1);

            CreateChatRoomRequest createChatRoomRequest = new CreateChatRoomRequest();
            createChatRoomRequest.setUsername("username-2");

            Mockito.when(chatEntityRepository.findByUsers(any(), any(), any())).thenReturn(chat);

            ChatEntity createdChat = chatService.createChatRoom(createChatRoomRequest);

            Assertions.assertEquals(createdChat.getId(), chat.getId());
            Assertions.assertEquals(createdChat.getType(), ChatEntity.ChatType.SINGLE);
            Assertions.assertEquals(createdChat.getUsers(), Set.of(user1.getUsername(), user2.getUsername()));
        }
    }
}