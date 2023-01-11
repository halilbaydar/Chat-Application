package com.chat.service;

import com.chat.interfaces.repository.UserRepository;
import com.chat.interfaces.service.UserService;
import com.chat.model.entity.UserEntity;
import com.mongodb.client.MongoDatabase;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.chat.constant.DocumentConstant.USERS;
import static com.chat.util.SessionUtil.getActiveUser;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final MongoDatabase mongoDatabase;

    @Override
    public <R> R getUser() {
        UserEntity activeUser = getActiveUser();
        activeUser.setPassword(null);
        activeUser.setRole(null);
        return (R) activeUser;
    }

    @Override
    public <R> R getUsers() {
        UserEntity activeUser = getActiveUser();
        List<Document> response = userRepository
                .findAllT(mongoDatabase, USERS, activeUser.getId());
        return (R) response
                .parallelStream()
                .peek(document -> {
                    document.remove("password");
                    document.remove("role");
                    document.remove("_class");
                })
                .collect(Collectors.toList());
    }
}
