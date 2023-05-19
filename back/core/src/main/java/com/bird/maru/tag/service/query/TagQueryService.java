package com.bird.maru.tag.service.query;

import com.bird.maru.domain.model.document.TagDoc;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface TagQueryService {

    List<TagDoc> searchTags(String keyword, Pageable pageable);

}
