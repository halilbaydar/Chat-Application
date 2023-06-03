package com.chat.controller;

import com.chat.model.request.SearchRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

//@WebFluxTest
@SpringBootTest
@AutoConfigureWebTestClient
class SearchControllerTest {
    @Autowired
    private WebTestClient webTestClient;
//    private final ElasticQueryWebClient elasticQueryWebClient;

    @Test
    void searchByName() {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setKeyword("halil");
        searchRequest.setPage(1);
        searchRequest.setDirection(Sort.Direction.DESC.name());
        searchRequest.setSort("name");
        Mono<SearchRequest> requestParam = Mono.just(searchRequest);

        Flux<Object> response = this.webTestClient
                .post()
                .uri("/v1/user/search/name")
                .body(requestParam, SearchRequest.class)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .returnResult(Object.class)
                .getResponseBody();

        StepVerifier
                .create(response)
                .consumeNextWith(search -> {
                    System.out.println(search);
                })
                .expectComplete()
                .verify();

    }
}