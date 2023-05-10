package com.chat.service.impl;

import com.chat.model.UserElasticEntity;
import com.chat.model.request.SearchRequest;
import com.chat.model.response.SearchResponse;
import com.chat.service.ElasticQueryWebClient;
import com.chat.util.ElasticQueryUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserElasticQueryWebClient implements ElasticQueryWebClient {
    private final ElasticQueryUtil<String, UserElasticEntity> elasticQueryUtil;
    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public Flux<SearchResponse> searchByName(SearchRequest searchRequest) {
        log.info("Querying by text {}", searchRequest.getKeyword());
        Query query = elasticQueryUtil.getSearchQueryByFieldText("name", searchRequest.getKeyword());
        SearchHits<UserElasticEntity> result = elasticsearchOperations.search(query, UserElasticEntity.class);
    }
}
