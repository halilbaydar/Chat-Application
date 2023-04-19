package com.chat.service;

import com.chat.constant.MessageStatus;
import com.chat.interfaces.service.MessageOperationService;
import com.chat.model.entity.ChatEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageOperationServiceImpl implements MessageOperationService {
    private final MongoTemplate mongoTemplate;

    @Override
    public void labelDeliverMessages(String chatId, String receiverId) {
        new Thread(() -> {
            Query validationQuery = new Query();
            validationQuery.addCriteria(new Criteria().andOperator(Criteria.where("id").is(chatId)));

            Update update = new Update()
                    .filterArray(Criteria.where("x.recipientId").is(receiverId)
                            .and("x.messageStatus").is(MessageStatus.SENT.name()))
                    .set("messages.$[x].messageStatus", MessageStatus.DELIVERED.name());
            mongoTemplate.updateFirst(validationQuery, update, ChatEntity.class);
        }).start();
    }

    @Override
    public void labelSeenMessages(String chatId, String receiverId) {
        new Thread(() -> {
            Query validationQuery = new Query();
            validationQuery.addCriteria(Criteria.where("id").is(chatId));
            Update update = new Update()
                    .filterArray(Criteria.where("x.recipientId").is(receiverId))
                    .set("messages.$[x].messageStatus", MessageStatus.SEEN.name());
            mongoTemplate.updateFirst(validationQuery, update, ChatEntity.class);
        }).start();
    }
}
