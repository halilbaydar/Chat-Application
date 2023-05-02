package com.chat.service;

import com.chat.interfaces.repository.UserRepository;
import com.chat.interfaces.service.UserService;
import com.chat.model.entity.UserEntity;
import com.chat.util.SessionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public <R> R getUser() {
        UserEntity activeUser = SessionUtil.getActiveUser();
        activeUser.setPassword(null);
        activeUser.setRole(null);
        return (R) activeUser;
    }

    @Override
    public <R> R getUsers() {
        return (R) userRepository.findAll();
    }
}
