package com.chat.interfaces.repository;

import com.chat.model.entity.UserEntity;
import com.chat.model.entity.UserResponse;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<UserEntity, String> {
    Mono<UserEntity> findByUsername(String username);

    Mono<Boolean> existsByUsername(String username);

    Flux<UserResponse> findAllByStatus(String status);
}
