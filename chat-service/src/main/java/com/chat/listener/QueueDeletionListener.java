package com.chat.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QueueDeletionListener implements ApplicationListener<ContextClosedEvent> {
    private final RabbitProperties rabbitProperties;
    private final RabbitAdmin rabbitAdmin;

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        this.rabbitAdmin.deleteQueue(rabbitProperties.getTemplate().getDefaultReceiveQueue());
    }
}
