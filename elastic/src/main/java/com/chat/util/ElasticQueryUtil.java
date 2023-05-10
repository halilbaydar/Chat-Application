package com.chat.util;

import com.chat.model.ElasticIndexModel;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collections;

import static com.chat.config.Pagination.DEFAULT_PAGE_SIZE;

@Component
public class ElasticQueryUtil<ID extends Serializable, T extends ElasticIndexModel<ID>> {

    public Query getSearchQueryById(ID id) {
        return new NativeSearchQueryBuilder()
                .withIds(Collections.singleton(id.toString()))
                .build();
    }

    public Query getSearchQueryByFieldText(String field, String text, com.chat.request.PageRequest pageRequest) {
        return new NativeSearchQueryBuilder()
                .withQuery(new BoolQueryBuilder()
                        .must(QueryBuilders.matchQuery(field, text))
                )
                .withPageable(PageRequest.of(pageRequest.getPage(), DEFAULT_PAGE_SIZE, pageRequest.getDirection()))
                .build();
    }

    public Query getSearchQueryByFieldTextAndShould(String field, String text, com.chat.request.PageRequest pageRequest) {
        return new NativeSearchQueryBuilder()
                .withQuery(new BoolQueryBuilder()
                        .should(QueryBuilders.matchQuery(field, text)))
                .withPageable(PageRequest.of(pageRequest.getPage(), DEFAULT_PAGE_SIZE, pageRequest.getDirection()))
                .build();
    }

    public Query getSearchQueryForAll(com.chat.request.PageRequest pageRequest) {
        return new NativeSearchQueryBuilder()
                .withPageable(PageRequest.of(pageRequest.getPage(), DEFAULT_PAGE_SIZE, pageRequest.getDirection()))
                .withQuery(new BoolQueryBuilder()
                        .must(QueryBuilders.matchAllQuery()))
                .build();
    }
}
