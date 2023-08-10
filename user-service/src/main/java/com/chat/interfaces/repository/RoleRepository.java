package com.chat.interfaces.repository;

import com.chat.model.entity.RoleEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface RoleRepository extends ReactiveCrudRepository<RoleEntity, BigInteger> {
}
