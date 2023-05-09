package com.bird.maru.tag.repository.query;

import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TagElasticSearchRepository {

    private final ElasticsearchOperations operations;

}
