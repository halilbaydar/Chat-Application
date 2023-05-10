package com.chat.index;

import reactor.core.publisher.Mono;

public interface ElasticIndexClient {
    Mono<Void> checkIndexExists();

    Mono<Void> createIndex();
}
