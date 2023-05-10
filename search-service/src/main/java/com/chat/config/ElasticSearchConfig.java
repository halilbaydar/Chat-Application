package com.chat.config;

import com.chat.index.ElasticIndexClient;
import com.chat.model.UserElasticEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class ElasticSearchConfig {
    private final ElasticIndexClient elasticIndexClient;

    @PostConstruct
    public Mono<Boolean> run() {
        return elasticIndexClient.createIndexes(UserElasticEntity.class)
                .subscribeOn(Schedulers.boundedElastic());
    }
}
