package com.chat.service;

import com.chat.constant.MessageStatus;
import com.chat.constant.NotificationType;
import com.chat.interfaces.service.ChatService;
import com.chat.interfaces.service.SessionService;
import com.chat.model.entity.ChatEntity;
import com.chat.model.entity.MessageEntity;
import com.chat.model.other.BroadCastNotification;
import com.chat.model.request.*;
import com.chat.property.RabbitMQProperties;
import com.chat.redis.RedisStorageManager;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

import static com.chat.constant.PrefixConstant.*;
import static java.util.concurrent.CompletableFuture.runAsync;

@Service
@AllArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final SimpUserRegistry simpUserRegistry;
    private final RedisStorageManager redisStorageManager;
    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQProperties rabbitProperties;
    private final SessionService sessionService;
    private final MongoTemplate mongoTemplate;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public boolean isUserConnected(String name) {
        return simpUserRegistry.getUsers().stream().parallel().anyMatch(user -> user.getName().equals(name));
    }

    @Override
    public void sendMessage(MessageRequest messageRequest) {
        runAsync(() -> saveMessageOperations(messageRequest));

        if (isUserConnected(messageRequest.getReceiverName())) {
            runAsync(() -> simpMessagingTemplate.convertAndSend(CHAT_DESTINATION_PREFIX + messageRequest.getChatId(),
                    messageRequest.getMessage()));
        } else if (redisStorageManager.redisTemplate.hasKey(messageRequest.getReceiverName())) {
            BroadCastNotification<MessageRequest> broadCastNotification = new BroadCastNotification<>();
            broadCastNotification.setNotificationType(NotificationType.MESSAGE);
            broadCastNotification.setPayload(messageRequest);
            runAsync(() -> rabbitTemplate.convertAndSend(rabbitProperties.getExchange(), rabbitProperties.getRoutingKey(), broadCastNotification));
        } else {
            //TODO send this message via notification
        }
    }

    @Override
    public void saveMessageOperations(MessageRequest messageRequest) {
        MessageEntity message = MessageEntity
                .builder()
                .ChatId(messageRequest.getChatId())
                .message(messageRequest.getMessage())
                .messageStatus(MessageStatus.SENT)
                .recipientId(messageRequest.getRecipientId())
                .senderId(messageRequest.getSenderId())
                .build();
        Query validationQuery = new Query();
        validationQuery.addCriteria(Criteria.where("id").is(messageRequest.getChatId()));
        Update update = new Update();
        update.push("messages", message);
        mongoTemplate.updateFirst(validationQuery, update, ChatEntity.class);
    }

    @Override
    public void seenMessage(SeenRequest seenRequest) {
        runAsync(() -> seenMessageOperations(seenRequest));
        if (isUserConnected(seenRequest.getReceiverName())) {
            runAsync(() -> simpMessagingTemplate.convertAndSend(String.format("%s%s/%s", MESSAGE_SEEN_DESTINATION_PREFIX,
                    seenRequest.getChatId(), seenRequest.getMessageId()), true));
        } else if (redisStorageManager.redisTemplate.hasKey(seenRequest.getReceiverName())) {
            BroadCastNotification<SeenRequest> broadCastNotification = new BroadCastNotification<>();
            broadCastNotification.setNotificationType(NotificationType.SEEN);
            broadCastNotification.setPayload(seenRequest);
            runAsync(() -> rabbitTemplate.convertAndSend(rabbitProperties.getExchange(), rabbitProperties.getRoutingKey(), broadCastNotification));
        } else {
            //TODO pass
        }
    }

    @Override
    public void seenMessageOperations(SeenRequest seenRequest) {
        Query validationQuery = new Query();
        validationQuery.addCriteria(Criteria.where("id").is(seenRequest.getChatId()));
        Update update = new Update()
                .filterArray(Criteria.where("x.recipientId").is(seenRequest.getRecipientId())
                        .and("x.messageStatus").is(MessageStatus.DELIVERED.toString()))
                .set("messages.$[x].messageStatus", MessageStatus.SEEN.name());
        mongoTemplate.updateFirst(validationQuery, update, ChatEntity.class);
    }

    @Override
    public void typing(TypingRequest typingRequest) {
        if (isUserConnected(typingRequest.getReceiverName())) {
            simpMessagingTemplate.convertAndSend(String.format("%s%s", CHAT_TYPING_DESTINATION_PREFIX,
                    typingRequest.getChatId()), true);
        } else if (redisStorageManager.redisTemplate.hasKey(typingRequest.getReceiverName())) {
            BroadCastNotification<TypingRequest> broadCastNotification = new BroadCastNotification<>();
            broadCastNotification.setNotificationType(NotificationType.TYPING);
            broadCastNotification.setPayload(typingRequest);
            rabbitTemplate.convertAndSend(rabbitProperties.getExchange(), rabbitProperties.getRoutingKey(), broadCastNotification);
        } else {
            //TODO pass
        }
    }

    @Override
    public void online(OnlineRequest onlineRequest) {
        BroadCastNotification<OnlineRequest> broadCastNotification = new BroadCastNotification<>();
        broadCastNotification.setNotificationType(NotificationType.ONLINE);
        broadCastNotification.setPayload(onlineRequest);
        rabbitTemplate.convertAndSend(rabbitProperties.getExchange(),
                rabbitProperties.getRoutingKey(), broadCastNotification);
    }

    @Override
    public void deliverMessage(DeliverRequest deliverRequest) {
        try {
            if (isUserConnected(deliverRequest.getReceiverName())) {
                simpMessagingTemplate.convertAndSend(String.format("%s%s/%S", MESSAGE_DESTINATION_PREFIX,
                        deliverRequest.getChatId(), deliverRequest.getMessageId()), MessageStatus.DELIVERED.name());
            } else if (sessionService.isUserConnectedGlobally(deliverRequest.getReceiverName())) {
                BroadCastNotification<DeliverRequest> broadCastNotification = new BroadCastNotification<>();
                broadCastNotification.setNotificationType(NotificationType.DELIVER);
                broadCastNotification.setPayload(deliverRequest);
                rabbitTemplate.convertAndSend(rabbitProperties.getExchange(),
                        rabbitProperties.getRoutingKey(), broadCastNotification);
            } else {
                //TODO do nothing
            }
        } catch (Exception ignore) {

        } finally {
            deliverMessageOperations(deliverRequest);
        }
    }

    @Override
    public void deliverMessageOperations(DeliverRequest deliverRequest) {
        Query validationQuery = new Query();
        validationQuery.addCriteria(new Criteria().andOperator(Criteria.where("id").is(deliverRequest.getChatId())));
        Update update = new Update()
                .filterArray(Criteria.where("x.recipientId").is(deliverRequest.getRecipientId())
                        .and("x.messageStatus").is(MessageStatus.SENT.toString()))
                .set("messages.$[x].messageStatus", MessageStatus.DELIVERED.toString());
        mongoTemplate.updateFirst(validationQuery, update, ChatEntity.class);
    }
}
