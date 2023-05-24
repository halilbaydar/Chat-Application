package com.chat.config

import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.NewTopic
import reactor.kotlin.core.publisher.toMono
import java.util.Map

class KafkaAdmin

fun main() {
    val admin = AdminClient.create(
        Map.of<String, Any>(
            CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG,
            "http://localhost:19092",
        )
    )


    admin.createTopics(listOf(NewTopic("demo-events2", 3, 3)))

    admin.deleteTopics(listOf("demo-events1"))

}