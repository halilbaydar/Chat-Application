package com.chat.service;

import com.chat.model.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitUserService {
    private final RabbitTemplate rabbitTemplate;
    private final RabbitProperties rabbitProperties;

    public UserDto getUserFromUserServiceByUsername(final String username) {
        return this.rabbitTemplate
                .convertSendAndReceiveAsType(
                        rabbitProperties.getTemplate().getExchange(),
                        "routing-user",
                        username,
                        message -> {
                            message.getMessageProperties().setReplyTo(this.rabbitProperties.getTemplate().getDefaultReceiveQueue());
                            message.getMessageProperties().setCorrelationId(this.rabbitProperties.getTemplate().getRoutingKey());
                            message.getMessageProperties().setReceivedRoutingKey(this.rabbitProperties.getTemplate().getRoutingKey());
                            return message;
                        },
                        ParameterizedTypeReference.forType(UserDto.class)
                );
    }
}
