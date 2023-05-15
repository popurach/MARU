package com.bird.maru.tag.repository.query;

import com.bird.maru.domain.model.document.TagDoc;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TagElasticSearchQueryRepository {

    private final ElasticsearchOperations operations;

    public List<TagDoc> searchTags(String keyword, Pageable pageable) {
        MatchQueryBuilder query = QueryBuilders.matchQuery("name", keyword)
                                               .fuzziness(Fuzziness.AUTO);

        SearchHits<TagDoc> searched = operations.search(
                new NativeSearchQueryBuilder()
                        .withQuery(query)
                        .withPageable(pageable)
                        .build()
                , TagDoc.class
        );

        return searched.getSearchHits()
                       .stream()
                       .map(SearchHit::getContent)
                       .collect(Collectors.toList());
    }

}
