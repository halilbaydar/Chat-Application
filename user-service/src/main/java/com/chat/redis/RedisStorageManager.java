package com.chat.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.ReactiveRedisConnection;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;

@Component
@Configuration
//@EnableRedisHttpSession //NOTE it conflicts with webflux
@RequiredArgsConstructor
//@Import({RedisProperties.class, RedissonAutoConfiguration.class})
public class RedisStorageManager {

//    private final RedisProperties redisProperties;
    //    private final ObjectMapper objectMapper;
    public ReactiveHashOperations<String, Object, Object> map;
    public ReactiveListOperations<String, Object> list;
    public ReactiveSetOperations<String, Object> set;
    public ReactiveValueOperations<String, Object> value;
    public ReactiveRedisConnection conn;
    public ReactiveRedisTemplate<String, Object> reactiveRedisTemplate;

//    @Bean
//    public ReactiveRedisConnectionFactory redisConnectionFactory(RedisConfiguration redisConfiguration) {
//
//        final SocketOptions socketOptions = SocketOptions.builder().connectTimeout(Duration.ofSeconds(60)).build();
//        final ClientOptions clientOptions = ClientOptions.builder().socketOptions(socketOptions).build();
//
//        LettuceClientConfiguration clientConfig = LettuceClientConfiguration
//                .builder()
//                .commandTimeout(Duration.ofSeconds(60))
//                .clientOptions(clientOptions)
//                .build();
//
//        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisConfiguration, clientConfig);
//        lettuceConnectionFactory.setValidateConnection(true);
//        lettuceConnectionFactory.afterPropertiesSet();
//
//        return lettuceConnectionFactory;
//    }

//    @Profile({"prod", "dev"})
//    @Bean
//    public RedisConfiguration getRedisClusterConfiguration() {
//        return new RedisClusterConfiguration(List.of(redisProperties.getHost() + ":" + redisProperties.getPort()));
//    }
//
//    @Profile({"local", "test"})
//    @Bean
//    public RedisConfiguration getRedisStandaloneConfiguration() {
//        return new RedisStandaloneConfiguration(redisProperties.getHost(), redisProperties.getPort());
//    }

    @Bean
    public ReactiveRedisTemplate<String, Object> redisTemplate(ReactiveRedisConnectionFactory reactiveRedisConnectionFactory) {
        RedisSerializer<Object> jsonSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ((Jackson2JsonRedisSerializer<?>) jsonSerializer).setObjectMapper(new ObjectMapper());

        RedisSerializationContext<String, Object> redisSerializationContext = RedisSerializationContext
                .<String, Object>newSerializationContext()
                .hashValue(RedisSerializationContext.SerializationPair.fromSerializer(jsonSerializer))
                .value(RedisSerializationContext.SerializationPair.fromSerializer(jsonSerializer))
                .string(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string()))
                .hashKey(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string()))
                .key(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string()))
                .build();

        ReactiveRedisTemplate<String, Object> reactiveRedisTemplate =
                new ReactiveRedisTemplate<>(reactiveRedisConnectionFactory, redisSerializationContext);

        this.map = reactiveRedisTemplate.opsForHash();
        this.list = reactiveRedisTemplate.opsForList();
        this.set = reactiveRedisTemplate.opsForSet();
        this.value = reactiveRedisTemplate.opsForValue();

        this.conn = Objects.requireNonNull(reactiveRedisTemplate.getConnectionFactory()).getReactiveConnection();

        this.reactiveRedisTemplate = reactiveRedisTemplate;
        return reactiveRedisTemplate;
    }
}
