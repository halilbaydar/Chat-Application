package com.chat.controller;

import com.chat.model.UserElasticEntity;
import com.chat.model.request.SearchRequest;
import com.chat.service.ElasticQueryWebClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.suggest.response.Suggest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/v1/user/search")
public class SearchController {

    private final ElasticQueryWebClient elasticQueryWebClient;

    @GetMapping(value = "/name")
    public Flux<SearchHit<UserElasticEntity>> searchByName(@Valid @RequestBody Mono<SearchRequest> searchRequest) {
        return elasticQueryWebClient.searchByName(searchRequest);
    }

    @GetMapping(value = "/name/hit")
    public Mono<Suggest> searchSuggest(@Valid @RequestBody Mono<SearchRequest> searchRequest) {
        return elasticQueryWebClient.searchSuggest(searchRequest);
    }
}
