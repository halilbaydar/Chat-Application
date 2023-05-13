package com.chat

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KafkaToElasticServiceApplication

fun main(args: Array<String>) {
    runApplication<KafkaToElasticServiceApplication>(*args)
}