package com.chat.consumer;

import com.chat.interfaces.common.Consumer;
import com.chat.interfaces.service.ChatService;
import com.chat.model.other.BroadCastNotification;
import com.chat.model.request.*;
import com.chat.property.RabbitMQProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class MessageConsumer implements Consumer {

    private final ChatService chatService;
    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQProperties rabbitMQProperties;
    private final ObjectMapper objectMapper;

    @Override
    @RabbitListener(queues = "${amqp.queue}")
    public void consumeMessage(BroadCastNotification broadCastNotification) {
        switch (broadCastNotification.getNotificationType()) {
            case SEEN: {
                SeenRequest seenRequest = objectMapper.convertValue(broadCastNotification.getPayload(), SeenRequest.class);
            }
            break;
            case ONLINE: {
                OnlineRequest onlineRequest = objectMapper.convertValue(broadCastNotification.getPayload(), OnlineRequest.class);
            }
            break;
            case TYPING: {
                TypingRequest typingRequest = objectMapper.convertValue(broadCastNotification.getPayload(), TypingRequest.class);
            }
            break;
            case MESSAGE: {
                MessageRequest messageRequest = objectMapper.convertValue(broadCastNotification.getPayload(), MessageRequest.class);
            }
            break;
            case DELIVER: {
                DeliverRequest deliverRequest = objectMapper.convertValue(broadCastNotification.getPayload(), DeliverRequest.class);
            }
            break;
        }
    }
}
