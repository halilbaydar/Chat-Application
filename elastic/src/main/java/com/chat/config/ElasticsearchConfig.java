package com.chat.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.reactive.ReactiveElasticsearchClient;
import org.springframework.data.elasticsearch.client.reactive.ReactiveRestClients;
import org.springframework.data.elasticsearch.config.AbstractReactiveElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.web.reactive.function.client.ExchangeStrategies;

@Configuration
@RequiredArgsConstructor
@EnableElasticsearchRepositories(basePackages = "com.chat")
public class ElasticsearchConfig extends AbstractReactiveElasticsearchConfiguration {

    private final ElasticConfigData elasticConfigData;

    @Bean
    public ReactiveElasticsearchClient reactiveElasticsearchClient() {
        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
//                .connectedTo(elasticConfigData.getConnectionUrl())
                .connectedToLocalhost()
                .withClientConfigurer(webClient -> ExchangeStrategies.builder()
                        .codecs(configurer -> configurer.defaultCodecs()
                                .maxInMemorySize(-1)).build())
                .withBasicAuth(elasticConfigData.getUsername(), elasticConfigData.getPassword())
                .build();

        return ReactiveRestClients.create(clientConfiguration);
    }

}
