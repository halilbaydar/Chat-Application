package com.chat.repository;

import com.chat.model.UserElasticEntity;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface UserElasticsearchQueryRepository extends ReactiveElasticsearchRepository<UserElasticEntity, String> {
    Flux<UserElasticEntity> findByName(String name);
}
