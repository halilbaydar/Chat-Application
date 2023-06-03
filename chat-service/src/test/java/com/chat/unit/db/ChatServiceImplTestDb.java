package com.chat.unit.db;

import com.chat.common.AbstractIT;
import com.chat.interfaces.repository.ChatEntityRepository;
import com.chat.model.entity.ChatEntity;
import com.chat.model.request.CreateChatRoomRequest;
import com.chat.rabbit.RabbitUserService;
import com.chat.service.ChatServiceImpl;
import com.chat.unit.db.config.MongoClientConfig;
import com.chat.unit.db.config.MongoDatabaseConfig;
import com.chat.util.SessionUtil;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static org.mockito.ArgumentMatchers.anyString;

@DataMongoTest(properties = {"spring.mongodb.embedded.version:4.4.16"})
@ExtendWith(value = {MockitoExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
@Import({MongoClientConfig.class, MongoDatabaseConfig.class})
class ChatServiceImplTestDb extends AbstractIT {

    @Autowired
    private ChatEntityRepository chatEntityRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Mock
    private RabbitUserService rabbitUserService;
    private MongoDatabase mongoDatabase;

    @Autowired
    private MongoClient mongoClient;
    //    @InjectMocks
    private ChatServiceImpl chatService;

    @BeforeEach
    public void setup() throws IOException {
        mongoDatabase = mongoClient.getDatabase("chatdb");
        chatService = new ChatServiceImpl(chatEntityRepository, mongoDatabase, rabbitUserService);
        Mockito.when(rabbitUserService.receiveUserFromUserService(anyString())).thenReturn(user2);
    }

    @AfterEach
    public void rollBack() {
        mongoDatabase.getCollection("chats").drop();
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

            ChatEntity createdChat = chatService.createChatRoom(createChatRoomRequest);

            Assertions.assertEquals(createdChat.getType(), ChatEntity.ChatType.SINGLE);
            Assertions.assertEquals(createdChat.getUsers(), Set.of(user1.getUsername(), user2.getUsername()));
        }
    }

    @Test
    void chatYourself() {
        try (MockedStatic<SessionUtil> mockedFactory = Mockito.mockStatic(SessionUtil.class)) {
            int id = ThreadLocalRandom.current().nextInt();

            ChatEntity chat = new ChatEntity();
            chat.setUsers(Set.of(user2.getUsername(), user3.getUsername()));
            chat.setType(ChatEntity.ChatType.SINGLE);
            chat.setCreatedDate(createdDate);
            chat.setId(String.valueOf(id));
            chatEntityRepository.save(chat);

            mockedFactory.when(SessionUtil::getActiveUser).thenReturn(user2);

            CreateChatRoomRequest createChatRoomRequest = new CreateChatRoomRequest();
            createChatRoomRequest.setUsername("username-2");

            RuntimeException exception =
                    Assertions.assertThrows(RuntimeException.class,
                            () -> chatService.createChatRoom(createChatRoomRequest));

            Assertions.assertEquals(exception.getMessage(), "YOU_CANNOT_CHAT_YOURSELF");
        }
    }


    @Test
    void getChatRoom() {
        try (MockedStatic<SessionUtil> mockedFactory = Mockito.mockStatic(SessionUtil.class)) {
            int id = ThreadLocalRandom.current().nextInt();

            ChatEntity chat = new ChatEntity();
            chat.setUsers(Set.of(user2.getUsername(), user3.getUsername()));
            chat.setType(ChatEntity.ChatType.SINGLE);
            chat.setCreatedDate(createdDate);
            chat.setId(String.valueOf(id));
            chatEntityRepository.save(chat);

            mockedFactory.when(SessionUtil::getActiveUser).thenReturn(user2);

            CreateChatRoomRequest createChatRoomRequest = new CreateChatRoomRequest();
            createChatRoomRequest.setUsername("username-3");

            ChatEntity createdChat = chatService.createChatRoom(createChatRoomRequest);

            Assertions.assertEquals(createdChat.getId(), chat.getId());
            Assertions.assertEquals(createdChat.getType(), ChatEntity.ChatType.SINGLE);
            Assertions.assertEquals(createdChat.getUsers(), Set.of(user2.getUsername(), user3.getUsername()));
        }
    }
}