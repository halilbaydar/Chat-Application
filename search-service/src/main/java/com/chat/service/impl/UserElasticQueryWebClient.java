package com.chat.service.impl;

import com.chat.config.ElasticQueryWebClientConfigData;
import com.chat.exception.ElasticQueryClientException;
import com.chat.model.request.SearchRequest;
import com.chat.model.response.SearchResponse;
import com.chat.service.ElasticQueryWebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserElasticQueryWebClient implements ElasticQueryWebClient {

    private static final Logger LOG = LoggerFactory.getLogger(UserElasticQueryWebClient.class);

    private final WebClient webClient;
    private final ElasticQueryWebClientConfigData elasticQueryWebClientConfig;

    public UserElasticQueryWebClient(@Qualifier("webClient") WebClient webClient,
                                     ElasticQueryWebClientConfigData elasticQueryWebClientConfig) {
        this.webClient = webClient;
        this.elasticQueryWebClientConfig = elasticQueryWebClientConfig;
    }

    @Override
    public Flux<SearchResponse> getDataByText(SearchRequest searchRequest) {
        LOG.info("Querying by text {}", searchRequest.getWord());
        return getWebClient(searchRequest)
                .bodyToFlux(SearchResponse.class);
    }

    private WebClient.ResponseSpec getWebClient(SearchRequest request) {
        return webClient
                .method(HttpMethod.valueOf(elasticQueryWebClientConfig.getQueryByText().getMethod()))
                .uri(elasticQueryWebClientConfig.getQueryByText().getUri())
                .accept(MediaType.valueOf(elasticQueryWebClientConfig.getQueryByText().getAccept()))
                .body(BodyInserters.fromPublisher(Mono.just(request), createParameterizedTypeReference()))
                .retrieve()
                .onStatus(
                        httpStatus -> httpStatus.equals(HttpStatus.UNAUTHORIZED),
                        clientResponse -> Mono.just(new BadCredentialsException("Not authenticated!")))
                .onStatus(
                        HttpStatus::is4xxClientError,
                        clientResponse -> Mono.just(
                                new ElasticQueryClientException(clientResponse.statusCode().getReasonPhrase())))
                .onStatus(
                        HttpStatus::is5xxServerError,
                        clientResponse -> Mono.just(new Exception(clientResponse.statusCode().getReasonPhrase())));
    }


    private <T> ParameterizedTypeReference<T> createParameterizedTypeReference() {
        return new ParameterizedTypeReference<>() {
        };
    }
}
