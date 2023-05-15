package com.bird.maru.tag.service;

import com.bird.maru.domain.model.entity.Tag;
import java.util.List;

public interface TagService {

    void saveTagsToElasticSearch(List<Tag> tags);

}
