package com.chat.redis;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.SocketOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.ConfigureRedisAction;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

@Component
@Configuration
//@EnableRedisHttpSession
@RequiredArgsConstructor
public class RedisStorageManager {

    private final RedisProperties redisProperties;

    public HashOperations map;

    public ListOperations list;
    public SetOperations set;
    public ValueOperations value;
    public RedisConnection conn;
    public RedisTemplate<Object, Object> redisTemplate;

    @Value("${spring.profiles.active}")
    private String profile;

    @Bean
    public RedisConnectionFactory redisConnectionFactory(RedisConfiguration redisConfiguration) {

        final SocketOptions socketOptions = SocketOptions.builder()
                .connectTimeout(Duration.ofSeconds(60))
                .tcpNoDelay(true)
                .build();

        final ClientOptions clientOptions = ClientOptions.builder()
                .socketOptions(socketOptions)
                .autoReconnect(true)
                .build();

        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder().commandTimeout(Duration.ofSeconds(60)).clientOptions(clientOptions).build();

        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisConfiguration, clientConfig);
        lettuceConnectionFactory.setValidateConnection(true);

        return lettuceConnectionFactory;
    }

    @Profile({"prod", "dev"})
    @Bean
    public RedisConfiguration getRedisClusterConfiguration() {
        return new RedisClusterConfiguration(List.of(redisProperties.getHost() + ":" + redisProperties.getPort()));
    }

    @Profile({"local", "test"})
    @Bean
    public RedisConfiguration getRedisStandaloneConfiguration() {
        return new RedisStandaloneConfiguration(redisProperties.getHost(), redisProperties.getPort());
    }

    @Bean
    public ConfigureRedisAction configureRedisAction() {
        return ConfigureRedisAction.NO_OP;
    }

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());

        redisTemplate.setStringSerializer(new StringRedisSerializer());

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));

        redisTemplate.setDefaultSerializer(new Jackson2JsonRedisSerializer<>(Object.class));

        redisTemplate.afterPropertiesSet();

        this.map = redisTemplate.opsForHash();
        this.list = redisTemplate.opsForList();
        this.set = redisTemplate.opsForSet();
        this.value = redisTemplate.opsForValue();

        this.conn = Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection();

        this.redisTemplate = redisTemplate;

        return redisTemplate;
    }
}
