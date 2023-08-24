package com.chat.interfaces.repository;

import com.chat.model.dto.UserResponse;
import com.chat.model.entity.UserEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Repository
public interface UserRepository extends ReactiveCrudRepository<UserEntity, BigInteger> {

    Mono<UserResponse> findByUsername(String username);

    Mono<Boolean> existsByUsername(String username);

    Flux<UserResponse> findAllByCreatedAtLessThanEqual(LocalDateTime localDateTime);
}
