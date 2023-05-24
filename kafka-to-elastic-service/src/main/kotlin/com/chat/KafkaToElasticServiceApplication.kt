package com.chat

import lombok.RequiredArgsConstructor
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@RequiredArgsConstructor
@SpringBootApplication(scanBasePackages = ["com.chat"])
class KafkaToElasticServiceApplication : CommandLineRunner {
    override fun run(vararg args: String?) {

    }
}

fun main(args: Array<String>) {
    runApplication<KafkaToElasticServiceApplication>(*args)
}