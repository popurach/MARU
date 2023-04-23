package com.bird.maru.notice.service;

import com.bird.maru.domain.model.entity.Member;
import com.bird.maru.notice.model.Message;
import com.bird.maru.notice.model.Notice;
import com.bird.maru.notice.repository.NoticeRepository;
import java.util.LinkedList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;

    @Override
    public Slice<Notice> findByMemberId(Long memberId, Pageable pageable) {
        return noticeRepository.findByMemberIdOrderByCreatedDateTimeDesc(memberId, pageable);
    }

    @Override
    public void saveNotice(Member member, Message message) {
        Notice notice = Notice.builder()
                              .memberId(member.getId())
                              .noticeToken(member.getAlarmToken())
                              .category(message.getCategory())
                              .content(message.getContent())
                              .build();

        noticeRepository.save(notice);
    }

    // 알림 보내는 기능

    @Override
    public void saveBulkNotice(List<Member> members, List<Message> messages) {
        List<Notice> notices = new LinkedList<>();

    }

    public void notifyAuctionClosingSoon() { // 모두에게 알려야지요

    }

    public void notifyTopBidderRevoked() {

    }

    public void notifyAuctionClosed() {

    }

    public void notifyBidSuccessful() {

    }

    public void notifyBidFailed() {

    }

    public void notifyOccupationPeriodEnd() {

    }

    public void notifyOccupationStart() {

    }

}
