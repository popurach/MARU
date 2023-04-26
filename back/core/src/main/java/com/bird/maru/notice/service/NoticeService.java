package com.bird.maru.notice.service;

import com.bird.maru.domain.model.entity.Member;
import com.bird.maru.notice.model.Notice;
import com.bird.maru.notice.model.NoticeRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface NoticeService {

    Slice<Notice> findByMemberId(Long memberId, Pageable pageable);

    void notifyAuctionClosingSoon(List<Member> members);

    void notifyTopBidderRevoked(NoticeRequestDto requestDto);

    void notifyAuctionClosed(List<Member> members);

    void notifyBidSuccessful(List<NoticeRequestDto> requests);

    void notifyBidFailed(List<NoticeRequestDto> requests);

    void notifyOccupationStart(List<NoticeRequestDto> requests);

    void notifyOccupationPeriodEnd(List<NoticeRequestDto> requests);

}
