package com.chat.interfaces.repository;

import com.chat.model.entity.UserEntity;
import com.chat.model.view.UserView;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Repository
public interface UserRepository extends ReactiveCrudRepository<UserEntity, BigInteger> {

    Mono<UserView> findByUsername(String username);

    Mono<Boolean> existsByUsername(String username);

    Flux<UserView> findAllByCreatedAtLessThanEqual(LocalDateTime localDateTime);
}
