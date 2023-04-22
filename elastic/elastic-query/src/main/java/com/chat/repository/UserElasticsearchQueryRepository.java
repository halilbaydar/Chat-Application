package com.chat.repository;

import com.chat.model.UserIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserElasticsearchQueryRepository extends ElasticsearchRepository<UserIndex, String> {

    List<UserIndex> findByText(String text);
}
