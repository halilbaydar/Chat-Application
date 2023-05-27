package com.chat.service;

import com.chat.interfaces.service.SessionService;
import com.chat.redis.ChatRedisProperties;
import com.chat.redis.RedisStorageManager;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import java.security.Principal;

import static com.chat.util.StringUtil.isBlank;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final RedisStorageManager redisStorageManager;
    private final RabbitProperties rabbitMQProperties;
    private final ChatRedisProperties chatRedisProperties;

    @Override
    public void connectSession(@NotBlank String name, Principal simpUser) {
        if (simpUser != null) {
            if (!redisStorageManager.redisTemplate.hasKey(name)) {
                redisStorageManager.map.put(chatRedisProperties.getOnlineUsersMap(), name, rabbitMQProperties.getTemplate().getRoutingKey());
            }
        }
    }

    @Override
    public void disConnectSession(@NotBlank String sessionId) {
        redisStorageManager.map.delete(chatRedisProperties.getOnlineUsersMap(), sessionId);
    }

    @Override
    public void subscribeSession(Message<?> message, String sessionId, Principal simpUser) {

    }

    @Override
    public boolean isUserConnectedGlobally(String username) {
        return !isBlank(username) && redisStorageManager.map.hasKey(chatRedisProperties.getOnlineUsersMap(), username);
    }

    @Override
    public void isSubscribeValid(String username, String senderId) {

    }
}
