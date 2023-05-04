package com.bird.maru.notice.controller;

import com.bird.maru.domain.model.type.CustomUserDetails;
import com.bird.maru.notice.controller.dto.NoticeResponseDto;
import com.bird.maru.notice.model.Notice;
import com.bird.maru.notice.service.NoticeService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public List<NoticeResponseDto> findNotice(@PageableDefault(size = 15) Pageable pageable, @AuthenticationPrincipal CustomUserDetails member) {
        final Slice<Notice> notices = noticeService.findByMemberId(2L, pageable);

        return notices.stream().map(NoticeResponseDto::new).collect(Collectors.toList());
    }

}
