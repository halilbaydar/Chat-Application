package com.chat.service;

import com.chat.model.UserIndex;

import java.util.List;

public interface ElasticIndexClient<T extends UserIndex> {
    List<String> save(List<T> documents);
}
