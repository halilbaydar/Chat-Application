package com.chat

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@EnableDiscoveryClient
@SpringBootApplication
class AssetServiceApplication

fun main(args: Array<String>) {
    runApplication<AssetServiceApplication>(*args)
}