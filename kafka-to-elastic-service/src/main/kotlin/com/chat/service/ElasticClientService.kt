package com.chat.service

import com.chat.kafka.avro.model.UserAvroModel
import com.chat.model.UserElasticEntity
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.*

@Service
class ElasticClientService(
        private val reactiveElasticsearchOperations: ReactiveElasticsearchOperations,
) {

    fun save(user: UserAvroModel): Mono<Void> {
        return this.reactiveElasticsearchOperations.save(
                UserElasticEntity
                        .builder()
                        .username(user.username.toString())
                        .id(user.id)
                        .name(user.name.toString())
                        .createdDate(Date(user.createdDate))
                        .build())
                .then()
    }

    fun saveAll(list: List<UserAvroModel>): Mono<Void> {
        return this.reactiveElasticsearchOperations.saveAll(
                Mono.just(list.map { user ->
                    UserElasticEntity
                            .builder()
                            .username(user.username.toString())
                            .id(user.id)
                            .name(user.name.toString())
                            .createdDate(Date(user.createdDate))
                            .build()
                }), UserElasticEntity::class.java
        ).then()
    }
}