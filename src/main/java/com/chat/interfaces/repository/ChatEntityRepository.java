package com.chat.interfaces.repository;

import com.chat.interfaces.common.ChatRepository;
import com.chat.model.entity.ChatEntity;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatEntityRepository extends ChatRepository<ChatEntity, String> {
    default ChatEntity findByUsers(MongoDatabase mongoDatabase, String username, String username1) {
        return mongoDatabase.getCollection("chats", ChatEntity.class)
                .aggregate(List.of(
                        new Document("$match", new Document("users", new Document("$all", List.of(username, username1)))),
                        new Document("$addFields",
                                new Document("_id",
                                        new Document("$toString", "$_id")
                                )
                        )
                ))
                .cursor().tryNext();
    }
}
