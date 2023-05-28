package com.chat.rabbit;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QueueUnBindingListener implements ApplicationListener<ContextClosedEvent> {
    private final RabbitProperties rabbitProperties;
    private final RabbitAdmin rabbitAdmin;
    private final Queue queue;
    private final TopicExchange topicExchange;

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        this.rabbitAdmin.removeBinding(BindingBuilder.bind(queue)
                .to(topicExchange).with(rabbitProperties.getTemplate().getRoutingKey()));
    }
}
