package com.chat.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UserLogger {
    public final Logger
            register = LoggerFactory.getLogger("register"),
            userKafka = LoggerFactory.getLogger("user:kafka");
}
