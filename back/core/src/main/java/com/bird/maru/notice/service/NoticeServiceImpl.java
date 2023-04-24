package com.bird.maru.notice.service;

import com.bird.maru.domain.model.entity.Member;
import com.bird.maru.notice.model.Message;
import com.bird.maru.notice.model.Notice;
import com.bird.maru.notice.model.NoticeRequestDto;
import com.bird.maru.notice.repository.CustomNoticeRepository;
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
    private final CustomNoticeRepository customNoticeRepository;

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

    private void saveNotice(NoticeRequestDto requestDto) {
        Member member = requestDto.getMember();
        Message message = requestDto.getMessage();

        Notice notice = Notice.builder()
                              .memberId(member.getId())
                              .noticeToken(member.getAlarmToken())
                              .category(message.getCategory())
                              .content(message.getContent())
                              .build();

        noticeRepository.save(notice);
    }

    // bulk insert 알림 보내는 기능

    @Override
    public void saveBulkNotice(List<NoticeRequestDto> requests) {

        List<Notice> notices = new LinkedList<>();

        for (NoticeRequestDto request :
                requests) {
            notices.add(
                    Notice.builder()
                          .memberId(request.getMember().getId())
                          .noticeToken(request.getMember().getAlarmToken())
                          .category(request.getMessage().getCategory())
                          .content(request.getMessage().getContent())
                          .build());
        }

        customNoticeRepository.bulkInsertNotices(notices);
    }

    public void notifyAuctionClosingSoon() { // 모두에게 알려야지요?
        // 모두에게 보내기 vs 경매에 참여한 회원만 찾아서 보내기

    }

    public void notifyTopBidderRevoked(NoticeRequestDto requestDto) {
        // 뻇긴 사람한테만 보내야지
    }

    public void notifyAuctionClosed() {
        // 모두에게 보내기 vs 경매에 참여한 회원만 찾아서 보내기
    }

    public void notifyBidSuccessful() {
        // 입찰에 성공하여 포인트 차감 되었음을 알림 -> 이것도 bulk 로 들어오겠지?
    }

    public void notifyBidFailed() {
        // 입찰에 실패하여 포인트가 지급되었음을 알림 -> 이것도 bulk 로
        // 사용자 목록, 포인트 목록, 랜드마크 정보
    }

    public void notifyOccupationPeriodEnd() {
        // 이것도 마찬가지로 bulk 로
    }

    public void notifyOccupationStart() {
        // 이것도 마찬가지로 bulk 로 처리
    }

}
