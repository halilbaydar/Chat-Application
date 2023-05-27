package com.chat.service;

import com.chat.constant.MessageStatus;
import com.chat.constant.NotificationType;
import com.chat.interfaces.service.ChatSocketService;
import com.chat.model.entity.ChatEntity;
import com.chat.model.entity.MessageEntity;
import com.chat.model.other.BroadCastNotification;
import com.chat.model.request.*;
import com.chat.redis.ChatRedisProperties;
import com.chat.redis.RedisStorageManager;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.chat.constant.PrefixConstant.*;
import static java.util.concurrent.CompletableFuture.runAsync;

@Service
@AllArgsConstructor
public class ChatSocketServiceImpl implements ChatSocketService {

    private final SimpUserRegistry simpUserRegistry;
    private final RedisStorageManager redisStorageManager;
    private final RabbitTemplate rabbitTemplate;
    private final RabbitProperties rabbitProperties;
    private final MongoTemplate mongoTemplate;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatRedisProperties chatRedisProperties;

    @Override
    public boolean isUserConnected(String name) {
        return simpUserRegistry.getUsers().stream().parallel().anyMatch(user -> user.getName().equals(name));
    }

    @Override
    public void sendMessage(MessageRequest messageRequest) {
        messageRequest.setId(UUID.randomUUID().toString());
        runAsync(() -> saveMessageOperations(messageRequest));

        if (isUserConnected(messageRequest.getRecipientId())) {
            runAsync(() -> simpMessagingTemplate.convertAndSend(MESSAGE_DESTINATION_PREFIX + messageRequest.getRecipientId(), messageRequest));
        } else if (Boolean.TRUE.equals(redisStorageManager.redisTemplate.hasKey(messageRequest.getRecipientId()))) {
            var routingKey = (String) this.redisStorageManager.map.get(chatRedisProperties.getOnlineUsersMap(), messageRequest.getRecipientId());
            BroadCastNotification<MessageRequest> broadCastNotification = new BroadCastNotification<>();
            broadCastNotification.setNotificationType(NotificationType.MESSAGE);
            broadCastNotification.setPayload(messageRequest);
            this.rabbitTemplate.convertAndSend("", "", "", message -> {
                message.getMessageProperties().setReplyTo("responseQueue");
                return message;
            });
            runAsync(() -> rabbitTemplate.convertAndSend(rabbitProperties.getTemplate().getExchange(), routingKey, broadCastNotification));
        } else {
            //TODO send this message via notification
        }
    }

    @Override
    public void saveMessageOperations(MessageRequest messageRequest) {
        MessageEntity message = MessageEntity.builder().chatId(messageRequest.getChatId()).message(messageRequest.getMessage()).messageStatus(MessageStatus.SENT).recipientId(messageRequest.getRecipientId()).senderId(messageRequest.getSenderId()).id(messageRequest.getId()).build();
        Query validationQuery = new Query();
        validationQuery.addCriteria(Criteria.where("id").is(messageRequest.getChatId()));
        Update update = new Update();
        update.push("messages", message);
        mongoTemplate.updateFirst(validationQuery, update, ChatEntity.class);
    }

    @Override
    public void seenMessage(SeenRequest seenRequest) {
        runAsync(() -> seenMessageOperations(seenRequest));
        if (isUserConnected(seenRequest.getRecipientId())) {
            runAsync(() -> simpMessagingTemplate.convertAndSend(String.format("%s%s", MESSAGE_SEEN_DESTINATION_PREFIX, seenRequest.getChatId()), true));
        } else if (Boolean.TRUE.equals(this.redisStorageManager.map.hasKey(chatRedisProperties.getOnlineUsersMap(), seenRequest.getRecipientId()))) {
            var routingKey = (String) this.redisStorageManager.map.get(chatRedisProperties.getOnlineUsersMap(), seenRequest.getRecipientId());
            BroadCastNotification<SeenRequest> broadCastNotification = new BroadCastNotification<>();
            broadCastNotification.setNotificationType(NotificationType.SEEN);
            broadCastNotification.setPayload(seenRequest);
            runAsync(() -> rabbitTemplate.convertAndSend(rabbitProperties.getTemplate().getExchange(), routingKey, broadCastNotification));
        } else {
            //TODO pass
        }
    }

    @Override
    public void seenMessageOperations(SeenRequest seenRequest) {
        Query validationQuery = new Query();
        validationQuery.addCriteria(Criteria.where("id").is(seenRequest.getChatId()));
        Update update = new Update().filterArray(Criteria.where("x.recipientId").is(seenRequest.getRecipientId()).and("x.messageStatus").is(MessageStatus.DELIVERED.toString())).set("messages.$[x].messageStatus", MessageStatus.SEEN.name());
        mongoTemplate.updateFirst(validationQuery, update, ChatEntity.class);
    }

    @Override
    public void typing(TypingRequest typingRequest) {
        if (isUserConnected(typingRequest.getReceiverName())) {
            simpMessagingTemplate.convertAndSend(String.format("%s%s/%s", CHAT_TYPING_DESTINATION_PREFIX, typingRequest.getChatId(), typingRequest.getRecipientId()), true);
        } else if (Boolean.TRUE.equals(redisStorageManager.map.hasKey(chatRedisProperties.getOnlineUsersMap(), typingRequest.getRecipientId()))) {
            var routingKey = (String) this.redisStorageManager.map.get(chatRedisProperties.getOnlineUsersMap(), typingRequest.getRecipientId());
            BroadCastNotification<TypingRequest> broadCastNotification = new BroadCastNotification<>();
            broadCastNotification.setNotificationType(NotificationType.TYPING);
            broadCastNotification.setPayload(typingRequest);
            rabbitTemplate.convertAndSend(rabbitProperties.getTemplate().getExchange(), routingKey, broadCastNotification);
        } else {
            //TODO pass
        }
    }

    @Override
    public void online(OnlineRequest onlineRequest) {
        BroadCastNotification<OnlineRequest> broadCastNotification = new BroadCastNotification<>();
        broadCastNotification.setNotificationType(NotificationType.ONLINE);
        broadCastNotification.setPayload(onlineRequest);
        //TODO implement here
//        rabbitTemplate.convertAndSend(rabbitProperties.getExchange(), rabbitProperties.getRoutingKey(), broadCastNotification);
    }

    @Override
    public void deliverMessageOperations(DeliverRequest deliverRequest) {
        Query validationQuery = new Query();
        validationQuery.addCriteria(new Criteria().andOperator(Criteria.where("id").is(deliverRequest.getChatId())));
        Update update = new Update().filterArray(Criteria.where("x.recipientId").is(deliverRequest.getRecipientId()).and("x.messageStatus").is(MessageStatus.SENT.toString())).set("messages.$[x].messageStatus", MessageStatus.DELIVERED.toString());
        mongoTemplate.updateFirst(validationQuery, update, ChatEntity.class);
    }
}
