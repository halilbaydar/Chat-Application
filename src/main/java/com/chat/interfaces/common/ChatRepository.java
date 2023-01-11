package com.chat.interfaces.common;


import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.ArrayList;
import java.util.List;

@NoRepositoryBean
public interface ChatRepository<T, ID> extends MongoRepository<T, ID> {

    default List<Document> findAllT(MongoDatabase mongoDatabase, String collection, String activeUserId) {
        return mongoDatabase.getCollection(collection)
                .aggregate(List.of(
                        new Document("$match", new Document("_id", new Document("$ne", new ObjectId(activeUserId)))),
                        new Document("$addFields",
                                new Document("id",
                                        new Document("$toString", "$_id")
                                )
                        ),
                        new Document("$project",
                                new Document("_id", 0)
                        )))
                .into(new ArrayList<>());
    }
}
