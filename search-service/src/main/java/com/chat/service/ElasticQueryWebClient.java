package com.chat.service;

import com.chat.model.UserElasticEntity;
import com.chat.model.request.SearchRequest;
import com.chat.model.response.SearchResponse;
import org.springframework.data.elasticsearch.core.SearchHit;
import reactor.core.publisher.Flux;

public interface ElasticQueryWebClient {

    Flux<SearchHit<UserElasticEntity>> searchByName(SearchRequest request);
}
