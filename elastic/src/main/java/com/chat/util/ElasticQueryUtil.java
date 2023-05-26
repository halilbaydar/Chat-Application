package com.chat.util;

import com.chat.model.ElasticIndexModel;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.reactive.ReactiveElasticsearchClient;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collections;

import static com.chat.config.Pagination.DEFAULT_PAGE_SIZE;

@Component
@RequiredArgsConstructor
public class ElasticQueryUtil<ID extends Serializable, T extends ElasticIndexModel<ID>> {
    private final ReactiveElasticsearchClient reactiveElasticsearchClient;

    public Query getSearchQueryById(ID id) {
        return new NativeSearchQueryBuilder()
                .withIds(Collections.singleton(id.toString()))
                .build();
    }

    public Query getSearchQueryByFieldText(String field, String text, com.chat.request.PageRequest pageRequest) {
        return new NativeSearchQueryBuilder()
                .withQuery(new BoolQueryBuilder()
                        .must(QueryBuilders.matchQuery(field, text)))
                .withPageable(PageRequest.of(pageRequest.getPage(), DEFAULT_PAGE_SIZE, Sort.Direction.valueOf(pageRequest.getDirection()), pageRequest.getSort()))
                .build();
    }

    public Query getSearchQueryByFieldTextAndShould(String field, String text, com.chat.request.PageRequest pageRequest) {
        return new NativeSearchQueryBuilder()
                .withQuery(new QueryStringQueryBuilder(String.format("*%s*", text))
                        .field(field)
                        .fuzziness(Fuzziness.TWO))
                .withQuery(new BoolQueryBuilder()
                        .should(QueryBuilders.matchQuery(field, text)
                                .fuzziness(Fuzziness.TWO)))
                .withQuery(new BoolQueryBuilder()
                        .should(QueryBuilders.prefixQuery(field, text)
                                .caseInsensitive(true)))
                .withPageable(PageRequest.of(pageRequest.getPage(), DEFAULT_PAGE_SIZE, Sort.Direction.ASC, pageRequest.getSort()))
                .build();
    }

    public Query getSearchQueryForAll(com.chat.request.PageRequest pageRequest) {
        return new NativeSearchQueryBuilder()
                .withPageable(PageRequest.of(pageRequest.getPage(),
                        DEFAULT_PAGE_SIZE,
                        Sort.Direction.valueOf(pageRequest.getDirection()), pageRequest.getSort()))
                .withQuery(new BoolQueryBuilder()
                        .must(QueryBuilders.matchAllQuery()))
                .build();
    }

    public Query getSuggesstionQuery(String field, String text, com.chat.request.PageRequest pageRequest) {
        return new NativeSearchQueryBuilder()
                .withQuery(new BoolQueryBuilder()
                        .should(QueryBuilders.prefixQuery(field, text)
                                .caseInsensitive(true)))
                .withFields("name", "username")
//                .withPageable(PageRequest.of(pageRequest.getPage(), DEFAULT_PAGE_SIZE, Sort.Direction.ASC, pageRequest.getSort()))
                .build();
    }

//    public SuggestBuilder suggest(String field, String keyword) {
//
//        CompletionSuggestionBuilder completionSuggestionBuilder = SuggestBuilders.completionSuggestion(field);
//        completionSuggestionBuilder.prefix(keyword, Fuzziness.TWO).analyzer("completion").skipDuplicates();
//        SearchRequest searchRequest =
//                new SuggestReq()
//                        .indices("users")
//                        .source(new SuggestBuilder().addSuggestion("users", completionSuggestionBuilder));
//        Flux<SearchHit> response = this.reactiveElasticsearchClient.search(searchRequest);
//
//        return new SuggestBuilder().addSuggestion("users", completionSuggestionBuilder);
//    }
}
