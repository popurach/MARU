package com.bird.maru.notice.service;

import com.bird.maru.domain.model.entity.Member;
import com.bird.maru.notice.model.Category;
import com.bird.maru.notice.model.Message;
import com.bird.maru.notice.model.Notice;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

@SpringBootTest
class NoticeServiceTest {

    @Autowired
    private NoticeServiceImpl noticeService;

    @Test
    @DisplayName("알림_저장_단건")
    @Disabled
    void noticeSaveTest() {
        //given
        final Member member = Member.builder().alarmToken("test_token").id(1L).build();
        final Message message = new Message();
        message.setCategory(Category.AUCTION);
        message.setContent("테스트!");

        //when
        for (int i = 0; i < 50; i++) {
            message.setContent("테스트 ! " + i);
            noticeService.saveNotice(member, message);
        }
        final Pageable pageable = Pageable.ofSize(20);
        final Slice<Notice> notices = noticeService.findByMemberId(member.getId(), pageable);

        //then
        Assertions.assertThat(notices.getSize()).isEqualTo(20);
    }

}