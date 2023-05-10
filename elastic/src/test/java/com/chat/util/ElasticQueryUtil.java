package com.chat.util;

import com.chat.model.ElasticIndexModel;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collections;

@Component
public class ElasticQueryUtil<ID extends Serializable, T extends ElasticIndexModel<ID>> {

    public Query getSearchQueryById(ID id) {
        return new NativeSearchQueryBuilder()
                .withIds(Collections.singleton(id.toString()))
                .build();
    }

    public Query getSearchQueryByFieldText(String field, String text) {
        return new NativeSearchQueryBuilder()
                .withQuery(new BoolQueryBuilder()
                        .must(QueryBuilders.matchQuery(field, text))
                )
                .build();
    }

    public Query getSearchQueryForAll() {
        return new NativeSearchQueryBuilder()
                .withPageable(PageRequest.of(0, 10, Sort.Direction.ASC))
                .withQuery(new BoolQueryBuilder()
                        .must(QueryBuilders.matchAllQuery()))
                .build();
    }
}
