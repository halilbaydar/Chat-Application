package com.chat.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "amqp")
public class RabbitMQProperties {
    private String queue;
    private String exchange;
    private String routingKey;
}
