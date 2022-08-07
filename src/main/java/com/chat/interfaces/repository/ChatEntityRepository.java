package com.chat.interfaces.repository;

import com.chat.interfaces.common.ChatRepository;
import com.chat.model.entity.ChatEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatEntityRepository extends ChatRepository<ChatEntity, String> {
}
