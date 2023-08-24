package com.chat.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KafkaToElasticObjectMapper {

    @Bean
    fun objectMapper(): ObjectMapper {
        var mapper = ObjectMapper()
        mapper.registerModule(JavaTimeModule())
        return mapper
    }
}