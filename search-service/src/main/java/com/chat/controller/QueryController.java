package com.chat.controller;

import com.chat.model.request.SearchRequest;
import com.chat.model.response.SearchResponse;
import com.chat.service.ElasticQueryWebClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import reactor.core.publisher.Flux;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class QueryController {

    private final ElasticQueryWebClient elasticQueryWebClient;

    @PostMapping(value = "/query-by-text")
    public ResponseEntity<Flux<SearchResponse>> queryByText(@Valid SearchRequest searchRequest, Model model) {
        return ResponseEntity.of(Optional.of(
                elasticQueryWebClient.getDataByText(searchRequest)
        ));
    }

}
