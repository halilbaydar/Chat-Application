package com.chat.service;

import com.chat.interfaces.service.SessionService;
import com.chat.redis.RedisStorageManager;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import java.security.Principal;

import static com.chat.util.StringUtil.isBlank;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final RedisStorageManager redisStorageManager;

    @Override
    public void connectSession(@NotBlank String name, Principal simpUser) {
        if (simpUser != null) {
            if (!redisStorageManager.redisTemplate.hasKey(name)) {
                redisStorageManager.value.set(name, simpUser);
            }
        }
    }

    @Override
    public void disConnectSession(@NotBlank String sessionId) {
        if (redisStorageManager.redisTemplate.hasKey(sessionId)) {
            redisStorageManager.redisTemplate.delete(sessionId);
        }
    }

    @Override
    public void subscribeSession(Message<?> message, String sessionId, Principal simpUser) {

    }

    @Override
    public boolean isUserConnectedGlobally(String username) {
        return !isBlank(username) && redisStorageManager.redisTemplate.hasKey(username);
    }

    @Override
    public void isSubscribeValid(String username, String senderId) {

    }
}
