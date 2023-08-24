package com.chat.util;

import com.chat.model.UserElasticEntity;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ElasticIndexUtil<T extends UserElasticEntity> {

    public List<IndexQuery> getIndexQueries(List<T> documents) {
        return documents.stream()
                .map(document -> new IndexQueryBuilder()
                        .withId(document.getId().toString())
                        .withObject(document)
                        .build()
                ).collect(Collectors.toList());
    }
}
