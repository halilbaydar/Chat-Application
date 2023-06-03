package com.chat.common;

import com.chat.model.common.Role;
import com.chat.model.entity.ChatEntity;
import com.chat.model.entity.message.RabbitUserEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.BeforeEach;

import java.math.BigInteger;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public abstract class AbstractIT {
    protected final ObjectMapper objectMapper = new ObjectMapper();
    protected final ObjectWriter objectWriter = objectMapper.writer();

    protected final RabbitUserEntity user1 = RabbitUserEntity
            .builder()
            .id(BigInteger.valueOf(ThreadLocalRandom.current().nextInt()))
            .createdAt(new Date().getTime())
            .updatedAt(new Date().getTime())
            .role(Role.USER.name())
            .password("password")
            .name("user-1")
            .username("username-1")
            .build();

    protected final RabbitUserEntity user2 = RabbitUserEntity
            .builder()
            .id(BigInteger.valueOf(ThreadLocalRandom.current().nextInt()))
            .createdAt(new Date().getTime())
            .updatedAt(new Date().getTime())
            .role(Role.USER.name())
            .password("password")
            .name("user-2")
            .username("username-2")
            .build();

    protected final RabbitUserEntity user3 = RabbitUserEntity
            .builder()
            .id(BigInteger.valueOf(ThreadLocalRandom.current().nextInt()))
            .createdAt(new Date().getTime())
            .updatedAt(new Date().getTime())
            .role(Role.USER.name())
            .password("password")
            .name("user-3")
            .username("username-3")
            .build();

    protected Date createdDate = new Date();
    protected int id = ThreadLocalRandom.current().nextInt();
    protected ChatEntity chat = new ChatEntity();

    @BeforeEach
    protected void globalSetup() {
        chat.setUsers(Set.of(user1.getUsername(), user2.getUsername()));
        chat.setType(ChatEntity.ChatType.SINGLE);
        chat.setCreatedDate(createdDate);
        chat.setId(String.valueOf(id));
    }
}
