package com.chat.service;

import com.chat.model.request.SearchRequest;
import com.chat.model.response.SearchResponse;
import reactor.core.publisher.Flux;

public interface ElasticQueryWebClient {

    Flux<SearchResponse> getDataByText(SearchRequest request);
}
