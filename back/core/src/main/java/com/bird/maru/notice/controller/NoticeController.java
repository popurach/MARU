package com.bird.maru.notice.controller;

import com.bird.maru.notice.model.Notice;
import com.bird.maru.notice.service.NoticeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping("/find")
    public List<Notice> findNotice() {
        return noticeService.findByMemberId(1L);
    }

}
