package com.chat.websocket;

import com.chat.model.common.Role;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.security.messaging.access.intercept.ChannelSecurityInterceptor;
import org.springframework.security.messaging.access.intercept.MessageSecurityMetadataSource;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

import static org.springframework.messaging.simp.SimpMessageType.*;


@Configuration
@EnableWebSocketMessageBroker
public class SecurityWebSocketMessageBrokerConfigurer extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
                .simpDestMatchers("/ws/**", "/app/**").hasAnyRole(Role.USER.name())
                .simpTypeMatchers(CONNECT,
                        CONNECT_ACK,
                        MESSAGE,
                        SUBSCRIBE,
                        UNSUBSCRIBE,
                        HEARTBEAT,
                        DISCONNECT,
                        DISCONNECT_ACK,
                        OTHER).hasAnyRole(Role.USER.name())
                .simpSubscribeDestMatchers("/user/**").hasAnyRole(Role.USER.name())
                .anyMessage().hasAnyRole(Role.USER.name())
                .nullDestMatcher().hasAnyRole(Role.USER.name());
    }

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}