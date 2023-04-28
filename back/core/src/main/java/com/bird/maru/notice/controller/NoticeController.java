package com.bird.maru.notice.controller;

import com.bird.maru.notice.model.Notice;
import com.bird.maru.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping
    public Slice<Notice> findNotice(@PageableDefault(size = 15) Pageable pageable) {
        return noticeService.findByMemberId(1L, pageable);
    }

}
