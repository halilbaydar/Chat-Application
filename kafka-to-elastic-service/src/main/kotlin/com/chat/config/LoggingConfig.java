package com.chat.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Component
public class LoggingConfig {
    public final Logger service = getLogger("service");
    public final Logger performance = getLogger("performance");
    public final Logger kafkaToElastic = getLogger("kafkaToElastic");

    private static Logger getLogger() {
        return LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    }

    private static Logger getLogger(String name) {
        return LoggerFactory.getLogger(name);
    }
}
