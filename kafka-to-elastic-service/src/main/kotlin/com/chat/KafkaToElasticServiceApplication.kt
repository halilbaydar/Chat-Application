package com.chat

import lombok.RequiredArgsConstructor
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@RequiredArgsConstructor
@SpringBootApplication(scanBasePackages = ["com.chat"])
class KafkaToElasticServiceApplication

fun main(args: Array<String>) {
    runApplication<KafkaToElasticServiceApplication>(*args)
}