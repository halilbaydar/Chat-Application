package com.chat.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.SocketOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.ConfigureRedisAction;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

@Component
@Configuration
//@EnableRedisHttpSession //NOTE it conflicts with webflux
@RequiredArgsConstructor
public class RedisStorageManager {

    private final RedisProperties redisProperties;

    public HashOperations<Object, Object, Serializable> map;
    public ListOperations<Object, Object> list;
    public SetOperations<Object, Object> set;
    public ValueOperations<Object, Object> value;
    public RedisConnection conn;
    public RedisTemplate<Object, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    @Value("${spring.profiles.active}")
    private String profile;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {

        final SocketOptions socketOptions = SocketOptions.builder().connectTimeout(Duration.ofSeconds(60)).build();
        final ClientOptions clientOptions = ClientOptions.builder().socketOptions(socketOptions).build();

        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder().commandTimeout(Duration.ofSeconds(60)).clientOptions(clientOptions).build();

        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(getRedisConfiguration(), clientConfig);
        lettuceConnectionFactory.setValidateConnection(true);

        return lettuceConnectionFactory;
    }

    private RedisConfiguration getRedisConfiguration() {
        if (!profile.equals("local")) return getRedisClusterConfiguration();
        return getRedisStandaloneConfiguration();
    }

    @Profile({"prod", "dev"})
    @Bean
    public RedisClusterConfiguration getRedisClusterConfiguration() {
        return new RedisClusterConfiguration(List.of(redisProperties.getHost() + ":" + redisProperties.getPort()));
    }

    @Profile({"local"})
    @Bean
    public RedisConfiguration getRedisStandaloneConfiguration() {
        return new RedisStandaloneConfiguration(redisProperties.getHost(), redisProperties.getPort());
    }

    @Bean
    public ConfigureRedisAction configureRedisAction() {
        return ConfigureRedisAction.NO_OP;
    }

    @Bean
    public RedisTemplate<Object, Object> redisTemplate() {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<Object, Object>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        RedisSerializer<Object> jsonSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ((Jackson2JsonRedisSerializer<?>) jsonSerializer).setObjectMapper(objectMapper);
        redisTemplate.setHashValueSerializer(jsonSerializer);

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
