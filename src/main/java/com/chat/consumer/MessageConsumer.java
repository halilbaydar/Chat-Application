package com.chat.consumer;

import com.chat.constant.MessageStatus;
import com.chat.interfaces.common.Consumer;
import com.chat.interfaces.service.ChatService;
import com.chat.interfaces.service.SessionService;
import com.chat.model.other.BroadCastNotification;
import com.chat.model.request.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import static com.chat.constant.PrefixConstant.*;
import static java.util.concurrent.CompletableFuture.runAsync;

@Component
@RequiredArgsConstructor
public class MessageConsumer implements Consumer {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ObjectMapper objectMapper;
    private final SessionService sessionService;
    private final ChatService chatService;

    @Override
    @RabbitListener(queues = "${amqp.queue}")
    public void consumeMessage(BroadCastNotification broadCastNotification) {
        switch (broadCastNotification.getNotificationType()) {
            case SEEN: {
                SeenRequest seenRequest = objectMapper.convertValue(broadCastNotification.getPayload(), SeenRequest.class);
                runAsync(() -> simpMessagingTemplate.convertAndSend(String.format("%s%s/%s", MESSAGE_SEEN_DESTINATION_PREFIX,
                        seenRequest.getChatId(), seenRequest.getMessageId()), true));
                runAsync(() -> chatService.seenMessageOperations(seenRequest));
            }
            break;
            case ONLINE: {
                OnlineRequest onlineRequest = objectMapper.convertValue(broadCastNotification.getPayload(), OnlineRequest.class);
                //TODO implement there
            }
            break;
            case TYPING: {
                TypingRequest typingRequest = objectMapper.convertValue(broadCastNotification.getPayload(), TypingRequest.class);
                if (sessionService.isUserConnectedGlobally(typingRequest.getReceiverName())) {
                    if (chatService.isUserConnected(typingRequest.getReceiverName())) {
                        simpMessagingTemplate.convertAndSend(String.format("%s%s", CHAT_TYPING_DESTINATION_PREFIX, typingRequest.getChatId()), true);
                    }
                }
            }
            break;
            case MESSAGE: {
                MessageRequest messageRequest = objectMapper.convertValue(broadCastNotification.getPayload(), MessageRequest.class);
                if (sessionService.isUserConnectedGlobally(messageRequest.getReceiverName())) {
                    if (chatService.isUserConnected(messageRequest.getReceiverName())) {
                        runAsync(() -> runAsync(() ->
                                simpMessagingTemplate.convertAndSend(CHAT_DESTINATION_PREFIX + messageRequest.getChatId(), messageRequest.getMessage())));
                        runAsync(() -> chatService.saveMessageOperations(messageRequest));
                    }
                }
            }
            break;
            case DELIVER: {
                DeliverRequest deliverRequest = objectMapper.convertValue(broadCastNotification.getPayload(), DeliverRequest.class);
                if (sessionService.isUserConnectedGlobally(deliverRequest.getReceiverName())) {
                    if (chatService.isUserConnected(deliverRequest.getReceiverName())) {
                        runAsync(() -> simpMessagingTemplate.convertAndSend(String.format("%s%s/%S", MESSAGE_DESTINATION_PREFIX,
                                deliverRequest.getChatId(), deliverRequest.getMessageId()), MessageStatus.DELIVERED.name()));
                        runAsync(() -> chatService.deliverMessageOperations(deliverRequest));
                    }
                }
            }
            break;
        }
    }
}
