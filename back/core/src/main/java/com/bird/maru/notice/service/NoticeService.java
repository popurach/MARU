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

    void notifyBidSuccessful(NoticeRequestDto requestDto);

    void notifyBidFailed(NoticeRequestDto requestDto);

    void notifyOccupationStart(NoticeRequestDto requestDto);

    void notifyOccupationPeriodEnd(NoticeRequestDto requestDto);

}
