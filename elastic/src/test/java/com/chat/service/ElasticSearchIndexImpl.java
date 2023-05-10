package com.chat.service;

import com.chat.index.ElasticIndexClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.data.elasticsearch.client.reactive.ReactiveElasticsearchClient;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ElasticSearchIndexImpl implements ElasticIndexClient {
    private final ReactiveElasticsearchClient reactiveElasticsearchClient;
    private final ReactiveElasticsearchOperations reactiveElasticsearchOperations;

    @Override
    public Mono<Void> checkIndexExists() {
        GetIndexRequest request = new GetIndexRequest();
        request.indices("MYMODEL_ES_INDEX");

        return reactiveElasticsearchClient.indices()
                .existsIndex(request)
                .doOnError(throwable -> log.error(throwable.getMessage(), throwable))
                .flatMap(indexExists -> {
                    log.info("Index {} exists: {}", "MYMODEL_ES_INDEX", indexExists);
                    if (!indexExists) {
                        return createIndex();
                    }
                    return Mono.empty();
                });
    }

    @Override
    public Mono<Void> createIndex() {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest();
        createIndexRequest.index("MYMODEL_ES_INDEX");
        createIndexRequest.mapping("DEFAULT_ES_DOC_TYPE",
                "{\n" +
                        "  \"properties\": {\n" +
                        "    \"timestamp\": {\n" +
                        "      \"type\": \"date\",\n" +
                        "      \"format\": \"epoch_millis||yyyy-MM-dd HH:mm:ss||yyyy-MM-dd\"\n" +
                        "    }\n" +
                        "  }\n" +
                        "}",
                XContentType.JSON);

        return reactiveElasticsearchClient.indices()
                .createIndex(createIndexRequest)
                .doOnSuccess(aVoid -> log.info("Created Index {}", "MYMODEL_ES_INDEX"))
                .doOnError(throwable -> log.error(throwable.getMessage(), throwable))
                .flatMap(it -> Mono.empty());
    }
}
