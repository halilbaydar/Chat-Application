package com.chat.service;

import com.chat.interfaces.repository.RoleRepository;
import com.chat.model.Role;
import com.chat.model.entity.RoleEntity;
import com.chat.model.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserRoleService {
    private final RoleRepository roleRepository;

    public Mono<RoleEntity> assignBaseRole(UserEntity userEntity) {
        var role = RoleEntity.assignBaseRole(userEntity);
        return roleRepository.save(role);
    }
}
