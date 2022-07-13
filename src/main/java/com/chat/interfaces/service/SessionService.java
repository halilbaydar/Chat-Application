package com.chat.interfaces.service;

import org.springframework.messaging.Message;

import java.security.Principal;

public interface SessionService {
    void connectSession(String sessionId, Principal simpUser);

    void disConnectSession(String sessionId);

    void subscribeSession(Message<?> message, String sessionId, Principal simpUser);

    boolean isUserConnectedGlobally(String username);

    void isSubscribeValid(String username, String senderId);
}
