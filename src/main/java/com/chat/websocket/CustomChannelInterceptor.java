package com.chat.websocket;

import com.chat.interfaces.service.JwtService;
import com.chat.interfaces.service.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@RequiredArgsConstructor
public class CustomChannelInterceptor implements ChannelInterceptor {

    private final JwtService jwtService;
    private final SessionService sessionService;

    @Override
    public void afterReceiveCompletion(Message<?> message, MessageChannel channel, Exception ex) {

    }

    @SneakyThrows
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor.getCommand() == null)
            return message;

        MessageHeaders headers = message.getHeaders();
        String sessionId = SimpMessageHeaderAccessor.getSessionId(headers);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            List<String> authorization = accessor.getNativeHeader("Authorization");

            assert authorization != null;
            String accessToken = authorization.get(0).split(" ")[1];

            String username = jwtService.getBody(accessToken).getSubject();

            List<Map<String, String>> authorities = (List<Map<String, String>>) jwtService.getBody(accessToken).get("authorities");

            Set<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities.stream()
                    .map(m -> new SimpleGrantedAuthority(m.get("authority"))).collect(Collectors.toSet());

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    simpleGrantedAuthorities
            );
            accessor.setUser(authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            sessionService.connectSession(authentication.getName(), authentication);
        }
        if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
            /*Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            sessionService.disConnectSession(authentication.getName());
            SecurityContextHolder.clearContext();
             */
        }
        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            List<String> authorization = accessor.getNativeHeader("Authorization");
            assert authorization != null;
            String accessToken = authorization.get(0).split(" ")[1];
            String username = jwtService.getBody(accessToken).getSubject();
            String senderId = accessor.getNativeHeader("senderId").get(0);
            sessionService.isSubscribeValid(username, senderId);
            Principal simpUser = SimpMessageHeaderAccessor.getUser(headers);
            sessionService.subscribeSession(message, sessionId, simpUser);
        }
        System.gc();
        System.runFinalization();
        return message;
    }
}