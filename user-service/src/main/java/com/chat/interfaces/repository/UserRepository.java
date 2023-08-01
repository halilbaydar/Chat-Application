package com.chat.interfaces.repository;

import com.chat.model.dto.UserDto;
import com.chat.model.dto.UserResponse;
import com.chat.model.entity.UserEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

@Repository
public interface UserRepository extends ReactiveCrudRepository<UserEntity, BigInteger> {

    Mono<UserEntity> findByUsername(String username);

    Mono<Boolean> existsByUsername(String username);

    Flux<UserResponse> findAllByStatus(String status);

    @Query("SELECT * FROM CHAT_USERS U,  CHAT_ROLES R, CHAT_PERMISSONS P, CHAT_USER_ROLES UR, CHAT_ROLE_PERMISSIONS RP WHERE U.USERNAME = :username " +
            " AND U.ID = UR.USER_ID AND UR.ROLE_ID = R.ID AND R.ID = RP.ROLE_ID AND RP.PERMISSION_ID = P.ID")
    Mono<UserDto> loadUserWithRolesByUsername(String username);
}
