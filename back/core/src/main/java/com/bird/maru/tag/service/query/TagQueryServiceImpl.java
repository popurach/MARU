package com.bird.maru.tag.service.query;

import com.bird.maru.domain.model.document.TagDoc;
import com.bird.maru.tag.repository.query.TagElasticSearchQueryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TagQueryServiceImpl implements TagQueryService {

    private final TagElasticSearchQueryRepository tagElasticSearchQueryRepository;

    @Override
    public List<TagDoc> searchTags(String keyword, Pageable pageable) {
        return tagElasticSearchQueryRepository.searchTags(keyword, pageable);
    }

}
