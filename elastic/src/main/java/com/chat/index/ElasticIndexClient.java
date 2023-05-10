package com.chat.index;

import com.chat.model.ElasticIndexModel;
import org.springframework.data.elasticsearch.support.StringObjectMap;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.concurrent.CompletableFuture;

public interface ElasticIndexClient {
    <ID extends Serializable> Mono<Boolean> createIndexes(Class<? extends ElasticIndexModel<ID>> index);

    <ID extends Serializable> Flux<CompletableFuture<? extends StringObjectMap<? extends StringObjectMap<?>>>> createIndex(Class<? extends ElasticIndexModel<ID>> index);
}
