package com.chat.service.impl;

import com.chat.log.ChatLogger;
import com.chat.model.UserElasticEntity;
import com.chat.model.request.SearchRequest;
import com.chat.service.ElasticQueryWebClient;
import com.chat.util.ElasticQueryUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ReactiveSearchHits;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserElasticQueryWebClient implements ElasticQueryWebClient {
    private final ElasticQueryUtil<String, UserElasticEntity> elasticQueryUtil;
    private final ElasticsearchOperations elasticsearchOperations;
    private final ReactiveElasticsearchOperations reactiveElasticsearchOperations;
    private final ChatLogger LOG;

    @Override
    public Flux<SearchHit<UserElasticEntity>> searchByName(SearchRequest searchRequest) {
        LOG.search.info("Querying by name {}", searchRequest.getKeyword());
        Query query = elasticQueryUtil.getSearchQueryByFieldTextAndShould("name", searchRequest.getKeyword(), searchRequest);
        return reactiveElasticsearchOperations.search(query, UserElasticEntity.class);
    }

    @Override
    public Mono<ReactiveSearchHits<UserElasticEntity>> searchByNameForHit(SearchRequest searchRequest) {
        LOG.search.info("Querying by name {}", searchRequest.getKeyword());
        Query query = elasticQueryUtil.getSearchQueryByFieldTextAndShould("name", searchRequest.getKeyword(), searchRequest);
        return reactiveElasticsearchOperations.searchForHits(query, UserElasticEntity.class);
    }
}
