package com.chat.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ChatLogger {
    public Logger s3 = LoggerFactory.getLogger("s3"),
            service = LoggerFactory.getLogger("service"),
            controller = LoggerFactory.getLogger("controller"),
            elastic = LoggerFactory.getLogger("elastic"),
            kafka = LoggerFactory.getLogger("kafka"),
            rabbitmq = LoggerFactory.getLogger("rabbitmq"),
            chat = LoggerFactory.getLogger("chat"),
            user = LoggerFactory.getLogger("user"),
            search = LoggerFactory.getLogger("search"),
            util = LoggerFactory.getLogger("util");
}
