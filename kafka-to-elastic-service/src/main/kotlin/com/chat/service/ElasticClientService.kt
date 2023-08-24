package com.chat.service

import com.chat.model.UserElasticEntity
import com.chat.model.UserModel
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@Service
class ElasticClientService(
    private val reactiveElasticsearchOperations: ReactiveElasticsearchOperations,
) {
    fun saveAll(list: List<UserModel>): Flux<UserElasticEntity> {
        return this.reactiveElasticsearchOperations.saveAll(
            Mono.just(list.map { user ->
                UserElasticEntity.builder().username(user.username).id(user.id).name(user.name)
                    .createdAt(user.createdAt).build()
            }), UserElasticEntity::class.java
        )
    }
}