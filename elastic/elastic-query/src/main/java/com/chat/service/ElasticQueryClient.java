package com.chat.service;

import com.chat.model.UserIndex;

import java.util.List;

public interface ElasticQueryClient<T extends UserIndex> {

    T getIndexModelById(String id);

    List<T> getIndexModelByText(String text);

    List<T> getAllIndexModels();
}
