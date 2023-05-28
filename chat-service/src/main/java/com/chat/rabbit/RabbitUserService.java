package com.chat.rabbit;

import com.chat.model.entity.message.RabbitUserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;

@Service
@RequiredArgsConstructor
public class RabbitUserService {
    private final RabbitProperties rabbitProperties;
    private final RabbitTemplate rabbitTemplate;

    public RabbitUserEntity receiveUserFromUserService(@NotBlank String username) {
        return this.rabbitTemplate.convertSendAndReceiveAsType(
                "userExchange",
                "routing-user",
                username,
                message -> {
                    message.getMessageProperties().setReplyTo(this.rabbitProperties.getTemplate().getDefaultReceiveQueue());
                    message.getMessageProperties().setCorrelationId(this.rabbitProperties.getTemplate().getRoutingKey());
                    message.getMessageProperties().setReceivedRoutingKey(this.rabbitProperties.getTemplate().getRoutingKey());
                    return message;
                },
                ParameterizedTypeReference.forType(RabbitUserEntity.class)
        );
    }
}
