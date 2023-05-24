package com.chat.service

import com.chat.kafka.avro.model.UserAvroModel
import com.chat.model.UserElasticEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchTemplate
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.*

@Service
class ElasticClientService(
        @Qualifier("elasticsearchOperations")
        @Autowired
        private val reactiveElasticsearchTemplate: ReactiveElasticsearchTemplate
) {

    fun save(user: UserAvroModel): Mono<Void> {
        return this.reactiveElasticsearchTemplate.save(
                UserElasticEntity
                        .builder()
                        .username(user.username.toString())
                        .id(user.id)
                        .name(user.name.toString())
                        .createdDate(Date(user.createdDate))
                        .build())
                .then()
    }
}