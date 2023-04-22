package com.chat.service.impl;

import com.chat.exception.ElasticQueryClientException;
import com.chat.model.UserIndex;
import com.chat.repository.UserElasticsearchQueryRepository;
import com.chat.service.ElasticQueryClient;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class TwitterElasticRepositoryQueryClient implements ElasticQueryClient<UserIndex> {
    private static final Logger LOG = LoggerFactory.getLogger(TwitterElasticRepositoryQueryClient.class);

    private final UserElasticsearchQueryRepository twitterElasticsearchQueryRepository;

    @Override
    public UserIndex getIndexModelById(String id) {
        Optional<UserIndex> searchResult = twitterElasticsearchQueryRepository.findById(id);
        LOG.info("Document with id {} retrieved successfully",
                searchResult.orElseThrow(() ->
                        new ElasticQueryClientException("No document found at elasticsearch with id " + id)).getId());
        return searchResult.get();
    }

    @Override
    public List<UserIndex> getIndexModelByText(String text) {
        List<UserIndex> searchResult = twitterElasticsearchQueryRepository.findByText(text);
        LOG.info("{} of documents with text {} retrieved successfully", searchResult.size(), text);
        return searchResult;
    }

    @Override
    public List<UserIndex> getAllIndexModels() {
        List<UserIndex> searchResult = StreamSupport.stream(twitterElasticsearchQueryRepository.findAll().spliterator(),
                false).collect(Collectors.toList());
        LOG.info("{} number of documents retrieved successfully", searchResult.size());
        return searchResult;
    }
}
