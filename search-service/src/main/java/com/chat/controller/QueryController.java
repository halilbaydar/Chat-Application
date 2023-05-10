package com.chat.controller;

import com.chat.model.request.SearchRequest;
import com.chat.model.response.SearchResponse;
import com.chat.service.ElasticQueryWebClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/v1/user/search")
public class QueryController {

    private final ElasticQueryWebClient elasticQueryWebClient;

    @PostMapping(value = "/name")
    public Flux<SearchResponse> searchByName(@Valid SearchRequest searchRequest) {
        return elasticQueryWebClient.searchByName(searchRequest);
    }

}
