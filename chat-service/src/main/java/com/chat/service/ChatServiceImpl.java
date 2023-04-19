package com.chat.service;

import com.chat.exception.CustomException;
import com.chat.interfaces.repository.ChatEntityRepository;
import com.chat.interfaces.repository.UserRepository;
import com.chat.interfaces.service.ChatService;
import com.chat.model.entity.ChatEntity;
import com.chat.model.entity.MessageEntity;
import com.chat.model.entity.UserEntity;
import com.chat.model.request.CreateChatRoomRequest;
import com.chat.model.request.GetMessagesRequest;
import com.mongodb.client.MongoDatabase;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import static com.chat.constant.DocumentConstant.CHATS;
import static com.chat.constant.ErrorConstant.ErrorMessage.CHAT_NOT_FOUND;
import static com.chat.constant.ErrorConstant.ErrorMessage.USER_NOT_EXIST;
import static com.chat.constant.GeneralConstant.PAGE_SIZE;
import static com.chat.util.SessionUtil.getActiveUser;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatEntityRepository chatRepository;
    private final MongoDatabase mongoDatabase;
    private final UserRepository userRepository;

    private static Integer getSkip(int pageNumber, long numberOfMessages) {
        int totalPages = (int) Math.ceil((numberOfMessages * 1D) / PAGE_SIZE);
        int skip = 0;

        if (pageNumber < 0 || pageNumber >= totalPages) {
            return -1;
        }
        if (pageNumber == 0) {//FIRST PAGE
            if (numberOfMessages > PAGE_SIZE) {
                skip = (int) (numberOfMessages - PAGE_SIZE);
            }
        } else if ((pageNumber + 1) == totalPages) {//LAST PAGE
            skip = PAGE_SIZE * (totalPages - pageNumber - 1);
        } else {//MIDDLE PAGE
            skip = (totalPages - pageNumber - 1) * PAGE_SIZE;
        }
        return skip;
    }

    @Override
    public List<?> getChatsByPagination(Integer pageNumber) {
        UserEntity activeUser = getActiveUser();
        List<?> response = mongoDatabase.getCollection(CHATS, ChatEntity.class).aggregate(List.of(
                new Document("$match", new Document("users", new Document("$all", List.of(activeUser.getUsername())))),
                new Document("$addFields",
                        new Document("id",
                                new Document("$toString", "$_id")
                        )
                ),
                new Document("$skip", PAGE_SIZE * pageNumber),
                new Document("$limit", PAGE_SIZE),
                new Document("$project",
                        new Document("users", 1)
                                .append("id", 1)
                                .append("type", 1)
                                .append("createdDate", 1)
                )
        )).into(new ArrayList<>());
        return response;
    }

    @Override
    public Stack<MessageEntity> getChatMessagesByPagination(GetMessagesRequest getMessagesRequest) {
        UserEntity activeUser = getActiveUser();

        Document document = mongoDatabase.getCollection(CHATS, Document.class).aggregate(List.of(
                new Document("$match", new Document("$and", List.of(
                        new Document("users", new Document("$in", List.of(activeUser.getUsername()))),
                        new Document("_id", new Document("$eq", new ObjectId(getMessagesRequest.getId())))
                ))),
                new Document("$addFields",
                        new Document("messagesSize",
                                new Document("$size", "$messages")
                        )
                ),
                new Document("$project", new Document("messagesSize", 1))
        )).first();
        if (document == null) {
            throw new RuntimeException(CHAT_NOT_FOUND);
        }
        long count = document.getInteger("messagesSize");
        int skip = getSkip(getMessagesRequest.getPageNumber(), count);
        if (skip == -1) {
            return new Stack<>();
        }
        try {
            ChatEntity chat = mongoDatabase.getCollection(CHATS, ChatEntity.class)
                    .aggregate(List.of(
                            new Document("$match", new Document("$and", List.of(
                                    new Document("users", new Document("$all", List.of(activeUser.getUsername()))),
                                    new Document("_id", new Document("$eq", new ObjectId(getMessagesRequest.getId())))
                            ))),
                            new Document("$addFields",
                                    new Document("messages",
                                            new Document("$slice", List.of("$messages", skip, PAGE_SIZE)))),
                            new Document("$addFields",
                                    new Document("_id",
                                            new Document("$toString", "$_id")
                                    )
                            ),
                            new Document("$project", new Document("messages", 1))
                    )).cursor().tryNext();

            if (chat == null) {
                throw new RuntimeException(CHAT_NOT_FOUND);
            }
            return chat.getMessages();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new Stack<>();
    }

    @Override
    public <Res> Res createChatRoom(CreateChatRoomRequest createChatRoomRequest) {
        UserEntity activeUser = getActiveUser();
        if (createChatRoomRequest.getUsername().equals(activeUser.getUsername())) {
            throw new RuntimeException("YOU_CANNOT_CHAT_YOURSELF");
        }
        boolean usernameExists = userRepository.existsByUsername(createChatRoomRequest.getUsername());
        if (!usernameExists) {
            throw new CustomException(USER_NOT_EXIST);
        }
        ChatEntity chatEntity = chatRepository.findByUsers(mongoDatabase, activeUser.getUsername(), createChatRoomRequest.getUsername());
        if (chatEntity != null) {
            chatEntity.setMessages(getChatMessagesByPagination(new GetMessagesRequest(0, chatEntity.getId())));
            return (Res) chatEntity;
        }
        ChatEntity chat = new ChatEntity();
        chat.setUsers(Set.of(activeUser.getUsername(), createChatRoomRequest.getUsername()));
        chat = chatRepository.save(chat);
        return (Res) chat;
    }
}
