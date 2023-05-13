package com.chat.service;

import com.chat.model.UserElasticEntity;
import com.chat.model.request.SearchRequest;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.suggest.response.Suggest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ElasticQueryWebClient {

    Flux<SearchHit<UserElasticEntity>> searchByName(SearchRequest request);

    Mono<Suggest> searchSuggest(SearchRequest searchRequest);
}
