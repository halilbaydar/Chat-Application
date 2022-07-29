package com.chat.websocket;

import com.chat.interfaces.service.JwtService;
import com.chat.interfaces.service.SessionService;
import com.chat.property.RabbitMQProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.converter.DefaultContentTypeResolver;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@RequiredArgsConstructor
public class WebSocketMessageBroker implements WebSocketMessageBrokerConfigurer {

    private static final String WS = "/ws";
    private static final String USER = "/user";
    private static final String APP = "/app";

    private final JwtService jwtService;
    private final SessionService sessionService;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker(USER);
        config.setApplicationDestinationPrefixes(APP);
        config.setUserDestinationPrefix(USER);
    }

    @Bean
    public CustomHandshakeInterceptor handshakeInterceptor() {
        return new CustomHandshakeInterceptor();
    }

    @Bean
    public CustomChannelInterceptor channelInterceptor() {
        return new CustomChannelInterceptor(jwtService, sessionService);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint(WS)
                .addInterceptors(handshakeInterceptor())
                .setAllowedOrigins("http://localhost:3000", "http://localhost:3001")
                .withSockJS();
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.setMessageSizeLimit(2000000000); // default : 64 * 1024
        registration.setSendTimeLimit(20 * 10000); // default : 10 * 10000
        registration.setSendBufferSizeLimit(30 * 512 * 1024); // default : 512 * 1024
        registration.setTimeToFirstMessage(99999); // Time
    }

    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        DefaultContentTypeResolver resolver = new DefaultContentTypeResolver();
        resolver.setDefaultMimeType(MimeTypeUtils.APPLICATION_JSON);
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(new ObjectMapper());
        converter.setContentTypeResolver(resolver);
        messageConverters.add(converter);
        return false;
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(channelInterceptor());
    }

    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        registration.interceptors(channelInterceptor());
        WebSocketMessageBrokerConfigurer.super.configureClientOutboundChannel(registration);
    }
}