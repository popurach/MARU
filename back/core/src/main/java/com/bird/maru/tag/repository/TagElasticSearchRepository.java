package com.bird.maru.tag.repository;

import com.bird.maru.domain.model.document.TagDoc;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TagElasticSearchRepository {

    private final ElasticsearchOperations operations;

    public void saveAll(List<TagDoc> tags) {
        operations.save(tags);
    }

}
