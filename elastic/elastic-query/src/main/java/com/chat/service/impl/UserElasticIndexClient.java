package com.chat.service.impl;

import com.chat.config.ElasticConfigData;
import com.chat.model.UserIndex;
import com.chat.service.ElasticIndexClient;
import com.chat.util.ElasticIndexUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexedObjectInformation;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserElasticIndexClient implements ElasticIndexClient<UserIndex> {

    private static final Logger LOG = LoggerFactory.getLogger(UserElasticIndexClient.class);

    private final ElasticConfigData elasticConfigData;

    private final ElasticsearchOperations elasticsearchOperations;

    private final ElasticIndexUtil<UserIndex> elasticIndexUtil;

    @Override
    public List<String> save(List<UserIndex> documents) {
        List<IndexQuery> indexQueries = elasticIndexUtil.getIndexQueries(documents);
        List<String> documentIds = elasticsearchOperations.bulkIndex(
                indexQueries,
                IndexCoordinates.of(elasticConfigData.getIndexName())
        ).stream().map(IndexedObjectInformation::getId).collect(Collectors.toList());
        LOG.info("Documents indexed successfully with type: {} and ids: {}", UserIndex.class.getName(),
                documentIds);
        return documentIds;
    }
}
