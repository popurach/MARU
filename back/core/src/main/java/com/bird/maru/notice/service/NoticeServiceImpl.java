package com.bird.maru.notice.service;

import com.bird.maru.domain.model.entity.Landmark;
import com.bird.maru.domain.model.entity.Member;
import com.bird.maru.cloud.firebase.fcm.FCMService;
import com.bird.maru.notice.model.Category;
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
    private final FCMService fcmService;

    @Override
    public Slice<Notice> findByMemberId(Long memberId, Pageable pageable) {
        return noticeRepository.findByMemberIdOrderByCreatedDateTimeDesc(memberId, pageable);
    }

    /**
     * 경매 알림 - 1 시간 뒤 마감
     *
     * @param members 알림을 보낼 회원 List
     */
    public void notifyAuctionClosingSoon(List<Member> members) {

        List<Notice> notices = new LinkedList<>();
        for (Member member :
                members) {
            notices.add(
                    Notice.builder().memberId(member.getId())
                          .noticeToken(member.getNoticeToken())
                          .category(Category.AUCTION)
                          .content("모든 랜드마크의 경매 마감 1시간 전 입니다. 서두르세요!")
                          .build());
        }

        customNoticeRepository.bulkInsertNotices(notices);

        // FCM Service 호출 추가 (다수)
        fcmService.sendMessageToMembers(notices);
    }

    /**
     * 상위 입찰자가 등장했음을 알림
     *
     * @param requestDto -> 빼앗긴 회원, 빼앗긴 랜드마크 정보 포함
     */
    public void notifyTopBidderRevoked(NoticeRequestDto requestDto) {
        // 뻇긴 사람한테만 보내야지
        Member member = requestDto.getMember();
        Landmark landmark = requestDto.getLandmark();

        Notice notice = Notice.builder().memberId(member.getId())
                              .noticeToken(member.getNoticeToken())
                              .category(Category.AUCTION)
                              .content(String.format("[%s] 경매에 상위 입찰자가 등장했습니다! 이번 경매에서 최고 입찰가를 유지하시겠어요?", landmark.getName()))
                              .build();

        noticeRepository.save(notice);

        // FCM Service 호출 추가 (단건)
        fcmService.sendMessage(notice);
    }

    /**
     * 경매가 종료되었음을 알림
     *
     * @param members 알림을 보낼 회원 목록
     */
    public void notifyAuctionClosed(List<Member> members) {
        List<Notice> notices = new LinkedList<>();

        for (Member member :
                members) {
            notices.add(
                    Notice.builder().memberId(member.getId())
                          .noticeToken(member.getNoticeToken())
                          .category(Category.AUCTION)
                          .content("모든 랜드마크의 경매가 마감되었습니다. 결과를 확인하세요!")
                          .build());
        }

        customNoticeRepository.bulkInsertNotices(notices);

        // FCM Service 호출 추가 (다수)
        fcmService.sendMessageToMembers(notices);
    }

    /**
     * 입찰에 성공하여 포인트가 차감되었음을 알림
     *
     * @param requests 회원, 랜드마크, 포인트 정보 포함
     */
    public void notifyBidSuccessful(List<NoticeRequestDto> requests) {
        List<Notice> notices = new LinkedList<>();

        for (NoticeRequestDto request :
                requests) {
            Member member = request.getMember();
            Landmark landmark = request.getLandmark();
            int point = request.getPoint();
            String content = String.format("[%s] 입찰에 성공하여 %d 포인트가 차감되었습니다.", landmark.getName(), point);

            notices.add(
                    Notice.builder()
                          .memberId(member.getId())
                          .noticeToken(member.getNoticeToken())
                          .category(Category.POINT)
                          .content(content).build());
        }

        customNoticeRepository.bulkInsertNotices(notices);
    }

    /**
     * 입찰 실패에 따른 포인트 지급 알림
     *
     * @param requests 회원, 랜드마크, 포인트 정보 포함 입찰 성공 메서드와 유사하므로 리팩토링을 통해 코드 중복 제거 필요
     */
    public void notifyBidFailed(List<NoticeRequestDto> requests) {
        List<Notice> notices = new LinkedList<>();

        for (NoticeRequestDto request :
                requests) {
            Member member = request.getMember();
            Landmark landmark = request.getLandmark();
            int point = request.getPoint();
            String content = String.format("[%s] 입찰에 실패하여 %d 포인트가 지급되었습니다.", landmark.getName(), point);

            notices.add(
                    Notice.builder()
                          .memberId(member.getId())
                          .noticeToken(member.getNoticeToken())
                          .category(Category.POINT)
                          .content(content).build());
        }

        customNoticeRepository.bulkInsertNotices(notices);
    }

    /**
     * 점령 시작 알림
     *
     * @param requests 회원, 랜드마크 정보 포함
     */
    public void notifyOccupationStart(List<NoticeRequestDto> requests) {
        List<Notice> notices = new LinkedList<>();

        for (NoticeRequestDto request :
                requests) {
            Member member = request.getMember();
            Landmark landmark = request.getLandmark();
            String content = String.format("[%s] 입찰에 성공하여 [%s]을(를) 일주일간 점령하게 되었습니다. 축하합니다!", landmark.getName(), landmark.getName());

            notices.add(
                    Notice.builder()
                          .memberId(member.getId())
                          .noticeToken(member.getNoticeToken())
                          .category(Category.LANDMARK)
                          .content(content)
                          .build());
        }

        customNoticeRepository.bulkInsertNotices(notices);
    }

    /**
     * 점령 종료 알림
     *
     * @param requests 회원 정보 포함
     */
    public void notifyOccupationPeriodEnd(List<NoticeRequestDto> requests) {
        List<Notice> notices = new LinkedList<>();

        for (NoticeRequestDto request :
                requests) {
            Member member = request.getMember();
            String content = "랜드마크의 점령 기간이 종료되었습니다. 새로운 경매에 참여해보세요!";

            notices.add(
                    Notice.builder()
                          .memberId(member.getId())
                          .noticeToken(member.getNoticeToken())
                          .category(Category.LANDMARK)
                          .content(content)
                          .build());
        }

        customNoticeRepository.bulkInsertNotices(notices);
    }

}
