package com.chat

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AssetServiceApplication {
    fun main(args: Array<String>) {
        runApplication<AssetServiceApplication>(*args)
    }
}