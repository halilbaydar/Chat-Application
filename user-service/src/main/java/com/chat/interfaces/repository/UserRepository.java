package com.chat.interfaces.repository;

import com.chat.model.entity.User;
import com.chat.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    User findByUsername(String username);

    boolean existsByUsername(String username);
}
