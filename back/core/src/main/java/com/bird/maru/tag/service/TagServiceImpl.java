package com.bird.maru.tag.service;

import com.bird.maru.domain.model.document.TagDoc;
import com.bird.maru.domain.model.entity.Tag;
import com.bird.maru.tag.repository.TagElasticSearchRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagElasticSearchRepository tagElasticSearchRepository;

    @Override
    public void saveTagsToElasticSearch(List<Tag> tags) {
        tagElasticSearchRepository.saveAll(
                tags.stream()
                    .map(TagDoc::new)
                    .collect(Collectors.toList())
        );
    }

}
