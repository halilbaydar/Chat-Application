package com.chat.interfaces.repository;

import com.chat.interfaces.common.ChatRepository;
import com.chat.model.entity.UserEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends ChatRepository<UserEntity, String> {
    UserEntity findByUsername(String username);
}
