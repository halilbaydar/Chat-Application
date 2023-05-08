package com.chat.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.lang.invoke.MethodHandles


fun getLogger(): Logger {
    return LoggerFactory.getLogger(MethodHandles.lookup().lookupClass())
}

fun getLogger(name: String): Logger {
    return LoggerFactory.getLogger(name)
}

@Component
class LoggingConfig {
    val s3 = getLogger("s3")
    val service = getLogger("service")
    val performance = getLogger("performance")
}