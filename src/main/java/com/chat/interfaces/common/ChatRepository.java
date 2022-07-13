package com.chat.interfaces.common;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ChatRepository<T, ID> extends MongoRepository<T, ID> {
}
