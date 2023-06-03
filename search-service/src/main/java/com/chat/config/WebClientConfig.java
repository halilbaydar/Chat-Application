package com.chat.config;
//
//import io.netty.channel.ChannelOption;
//import io.netty.handler.timeout.ReadTimeoutHandler;
//import io.netty.handler.timeout.WriteTimeoutHandler;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.client.reactive.ReactorClientHttpConnector;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.netty.http.client.HttpClient;
//
//import java.util.concurrent.TimeUnit;
//
//@Configuration
//@RequiredArgsConstructor
//public class WebClientConfig {
//
//    private final ElasticQueryWebClientConfigData webClientConfig;
//
//    @Bean("webTestClient")
//    public WebClient webClient() {
//        return WebClient
//                .builder()
//                .baseUrl(webClientConfig.getWebClient().getBaseUrl())
//                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.getType())
//                .clientConnector(new ReactorClientHttpConnector(getHttpClient()))
//                .codecs(configurer -> configurer
//                        .defaultCodecs()
//                        .maxInMemorySize(webClientConfig
//                                .getWebClient()
//                                .getMaxInMemorySize()))
//                .build();
//    }
//
//    private HttpClient getHttpClient() {
//        return HttpClient.create()
//                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,
//                        webClientConfig.getWebClient().getConnectTimeoutMs())
//                .doOnConnected(connection -> {
//                    connection.addHandlerLast(new ReadTimeoutHandler(
//                            webClientConfig.getWebClient().getReadTimeoutMs(),
//                            TimeUnit.MILLISECONDS));
//                    connection.addHandlerLast(new WriteTimeoutHandler(
//                            webClientConfig.getWebClient().getWriteTimeoutMs(),
//                            TimeUnit.MILLISECONDS));
//                });
//    }
//
//}
