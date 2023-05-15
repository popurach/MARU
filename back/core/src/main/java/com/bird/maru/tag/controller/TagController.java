package com.bird.maru.tag.controller;

import com.bird.maru.domain.model.document.TagDoc;
import com.bird.maru.tag.service.query.TagQueryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagQueryService tagQueryService;

    @GetMapping
    public List<TagDoc> searchTags(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "5", required = false) Integer size
    ) {
        if (!StringUtils.hasText(keyword)) {
            throw new IllegalArgumentException("검색어가 유효하지 않습니다.");
        }

        return tagQueryService.searchTags(keyword, Pageable.ofSize(size));
    }

}
