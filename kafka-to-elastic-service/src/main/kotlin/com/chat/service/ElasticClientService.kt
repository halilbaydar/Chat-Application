package com.chat.service

import com.chat.model.UserElasticEntity
import com.chat.model.UserModel
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@Service
class ElasticClientService(
    private val reactiveElasticsearchOperations: ReactiveElasticsearchOperations,
) {
    fun saveAll(list: List<UserModel>): Flux<UserElasticEntity> {
        return this.reactiveElasticsearchOperations.saveAll(
            Mono.just(list.map { user ->
                UserElasticEntity.builder().username(user.username).id(user.id).name(user.name).createdAt(
                        Date.from(
                            LocalDateTime.parse(user.createdAt.toString()).atZone(ZoneId.systemDefault()).toInstant()
                        )
                    ).build()
            }), UserElasticEntity::class.java
        )
    }
}