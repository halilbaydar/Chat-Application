package com.chat.service.impl;

import com.chat.config.ChatLogger;
import com.chat.model.UserElasticEntity;
import com.chat.model.request.SearchRequest;
import com.chat.service.ElasticQueryWebClient;
import com.chat.util.ElasticQueryUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.suggest.response.Suggest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserElasticQueryWebClient implements ElasticQueryWebClient {
    private final ElasticQueryUtil<String, UserElasticEntity> elasticQueryUtil;
    private final ReactiveElasticsearchOperations reactiveElasticsearchOperations;
    private final ChatLogger LOG;

    @Override
    public Flux<SearchHit<UserElasticEntity>> searchByName(Mono<SearchRequest> searchRequest) {
        return searchRequest.doOnNext(s -> {
                    LOG.search.info("Querying by name {}", s.getKeyword());
                })
                .map(s -> elasticQueryUtil.getSearchQueryByFieldTextAndShould("name", s.getKeyword(), s))
                .flatMapMany(q -> reactiveElasticsearchOperations.search(q, UserElasticEntity.class));
    }

    @Override
    public Mono<Suggest> searchSuggest(Mono<SearchRequest> searchRequest) {
        return searchRequest.doOnNext(s -> {
                    LOG.search.info("Querying by name {}", s.getKeyword());
                })
                .map(s -> elasticQueryUtil.getSuggesstionQuery("name", s.getKeyword(), s))
                .flatMap(suggesstion -> reactiveElasticsearchOperations.suggest(suggesstion, UserElasticEntity.class));
    }
}
