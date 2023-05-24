package com.chat.service;

import com.chat.config.ChatLogger;
import com.chat.index.ElasticIndexClient;
import com.chat.model.ElasticIndexModel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.index.Settings;
import org.springframework.data.elasticsearch.support.StringObjectMap;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class ElasticSearchIndexImpl implements ElasticIndexClient {
    private final ReactiveElasticsearchOperations reactiveElasticsearchOperations;
    private final ChatLogger LOG;

    @Override
    public <ID extends Serializable> Mono<Boolean> createIndexes(Class<? extends ElasticIndexModel<ID>> index) {
        return reactiveElasticsearchOperations
                .indexOps(index)
                .exists()
                .doOnNext(exists -> {
                    if (!exists) {
                        createIndex(index);
                    }
                })
                .doOnError(error -> {
                    LOG.elastic.error("Error while indexing users document with an error message: ${}", error.getMessage());
                })
                .doOnSuccess(success -> {
                    LOG.elastic.info("Indexes created successfully message: ${}, class name: ${}", success, index.getName());
                });
    }

    @Override
    public <ID extends Serializable> Flux<CompletableFuture<? extends StringObjectMap<? extends StringObjectMap<?>>>> createIndex(Class<? extends ElasticIndexModel<ID>> index) {

        CompletableFuture<Document> documentCompletableFuture = reactiveElasticsearchOperations
                .indexOps(index)
                .createMapping(index)
                .toFuture();

        CompletableFuture<Settings> settingsCompletableFuture = reactiveElasticsearchOperations
                .indexOps(index)
                .createSettings(index)
                .toFuture();

        CompletableFuture<Document> mappingCompletableFuture = reactiveElasticsearchOperations
                .indexOps(index)
                .createMapping(index)
                .toFuture();

        return Flux.fromIterable(List.of(documentCompletableFuture, settingsCompletableFuture, mappingCompletableFuture))
                .subscribeOn(Schedulers.boundedElastic());
    }
}
