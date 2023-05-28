package com.chat.consumer;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = "${spring.rabbitmq.template.default-receive-queue}", autoDelete = "false",
                arguments = @Argument(name = "x-message-ttl", value = "10000",
                        type = "java.lang.Integer")),
        exchange = @Exchange(value = "${spring.rabbitmq.template.exchange}", type = ExchangeTypes.TOPIC, autoDelete = "false")
//        arguments = {
//                @Argument(name = "x-match", value = "all"),
//                @Argument(name = "foo", value = "bar"),
//                @Argument(name = "baz")
//        }
        )
)
public @interface UserRequestsListener {
}
