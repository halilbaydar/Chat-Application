package com.chat.service;

import com.chat.model.UserElasticEntity;
import com.chat.model.request.SearchRequest;
import org.springframework.data.elasticsearch.core.ReactiveSearchHits;
import org.springframework.data.elasticsearch.core.SearchHit;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ElasticQueryWebClient {

    Flux<SearchHit<UserElasticEntity>> searchByName(SearchRequest request);

    Mono<ReactiveSearchHits<UserElasticEntity>> searchByNameForHit(SearchRequest searchRequest);
}
