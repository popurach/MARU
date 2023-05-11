//package com.bird.maru.notice.service;
//
//import com.bird.maru.domain.model.entity.Landmark;
//import com.bird.maru.domain.model.entity.Member;
//import com.bird.maru.notice.model.Category;
//import com.bird.maru.notice.model.Notice;
//import com.bird.maru.notice.model.NoticeRequestDto;
//import com.bird.maru.notice.repository.NoticeRepository;
//import java.util.ArrayList;
//import java.util.List;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Slice;
//
//@SpringBootTest
//class NoticeServiceImplTest {
//
//    @Autowired
//    private NoticeService noticeService;
//
//    @Autowired
//    private NoticeRepository noticeRepository;
//
//    @Test
//    @DisplayName("회원Id로 조회")
//    void findByMemberId() {
//        final Notice notice = Notice.builder().memberId(1L)
//                                    .noticeToken("notice_test_token")
//                                    .category(Category.AUCTION)
//                                    .content("notice test content").build();
//
//        noticeRepository.save(notice);
//
//        final Slice<Notice> notices = noticeService.findByMemberId(1L, Pageable.ofSize(20));
//        Assertions.assertThat(notices.getContent().get(0).getNoticeToken()).isEqualTo("notice_test_token");
//    }
//
//    @Test
//    @DisplayName("경매 종료 알림")
//    void notifyAuctionClosingSoon() {
//        final Member memberA = Member.builder()
//                                     .id(2L)
//                                     .noticeToken("member_A_Token")
//                                     .email("memberA_email")
//                                     .nickname("memberA")
//                                     .build();
//        final Member memberB = Member.builder()
//                                     .id(3L)
//                                     .noticeToken("member_A_Token")
//                                     .email("memberA_email")
//                                     .nickname("memberA")
//                                     .build();
//        List<Member> members = new ArrayList<>();
//        members.add(memberA);
//        members.add(memberB);
//
//        noticeService.notifyAuctionClosingSoon(members);
//
//        final Slice<Notice> resultA = noticeService.findByMemberId(memberA.getId(), Pageable.ofSize(20));
//        final Slice<Notice> resultB = noticeService.findByMemberId(memberB.getId(), Pageable.ofSize(20));
//
//        Assertions.assertThat(resultA.getContent().get(0).getContent()).isEqualTo("모든 랜드마크의 경매 마감 1시간 전 입니다. 서두르세요!");
//        Assertions.assertThat(resultB.getContent().get(0).getContent()).isEqualTo("모든 랜드마크의 경매 마감 1시간 전 입니다. 서두르세요!");
//
//    }
//
//    @Test
//    @DisplayName("상위 입찰자 발생 알림")
//    void notifyTopBidderRevoked() {
//        final NoticeRequestDto request = new NoticeRequestDto();
//
//        final Member memberA = Member.builder()
//                                     .id(2L)
//                                     .noticeToken("member_A_Token")
//                                     .email("memberA_email")
//                                     .nickname("memberA")
//                                     .build();
//
//        final Landmark landmarkA = Landmark.builder().name("Landmark A").build();
//
//        request.setLandmark(landmarkA);
//        request.setMember(memberA);
//
//        noticeService.notifyTopBidderRevoked(request);
//
//        final Slice<Notice> resultA = noticeService.findByMemberId(memberA.getId(), Pageable.ofSize(20));
//
//        Assertions.assertThat(resultA.getContent().get(0).getContent()).contains("경매에 상위 입찰자가 등장했습니다! 이번 경매에서 최고");
//
//    }
//
//    @Test
//    @DisplayName("경매 종료 알림")
//    void notifyAuctionClosed() {
//        final Member memberA = Member.builder()
//                                     .id(2L)
//                                     .noticeToken("member_A_Token")
//                                     .email("memberA_email")
//                                     .nickname("memberA")
//                                     .build();
//        final Member memberB = Member.builder()
//                                     .id(3L)
//                                     .noticeToken("member_A_Token")
//                                     .email("memberA_email")
//                                     .nickname("memberA")
//                                     .build();
//        List<Member> members = new ArrayList<>();
//        members.add(memberA);
//        members.add(memberB);
//
//        noticeService.notifyAuctionClosed(members);
//
//        final Slice<Notice> resultA = noticeService.findByMemberId(memberA.getId(), Pageable.ofSize(20));
//        final Slice<Notice> resultB = noticeService.findByMemberId(memberB.getId(), Pageable.ofSize(20));
//
//        Assertions.assertThat(resultA.getContent().get(0).getContent()).isEqualTo("모든 랜드마크의 경매가 마감되었습니다. 결과를 확인하세요!");
//        Assertions.assertThat(resultB.getContent().get(0).getContent()).isEqualTo("모든 랜드마크의 경매가 마감되었습니다. 결과를 확인하세요!");
//
//    }
//
//    @Test
//    @DisplayName("입찰 성공 알림")
//    void notifyBidSuccessful() {
//        // 포인트가 차감되었습니다.
//
//        final NoticeRequestDto request = new NoticeRequestDto();
//
//        final Member memberA = Member.builder()
//                                     .id(2L)
//                                     .noticeToken("member_A_Token")
//                                     .email("memberA_email")
//                                     .nickname("memberA")
//                                     .build();
//
//        final Landmark landmarkA = Landmark.builder().name("Landmark A").build();
//
//        request.setLandmark(landmarkA);
//        request.setMember(memberA);
//        request.setPoint(100);
//
//        noticeService.notifyBidSuccessful(request);
//
//        final Slice<Notice> resultA = noticeService.findByMemberId(memberA.getId(), Pageable.ofSize(20));
//
//        Assertions.assertThat(resultA.getContent().get(0).getContent()).contains("포인트가 차감되었습니다.");
//
//    }
//
//    @Test
//    @DisplayName("경매 입찰 실패 알림")
//    void notifyBidFailed() {
//        List<NoticeRequestDto> requests = new ArrayList<>();
//
//        final NoticeRequestDto request = new NoticeRequestDto();
//
//        final Member memberA = Member.builder()
//                                     .id(2L)
//                                     .noticeToken("member_A_Token")
//                                     .email("memberA_email")
//                                     .nickname("memberA")
//                                     .build();
//
//        final Landmark landmarkA = Landmark.builder().name("Landmark A").build();
//
//        request.setLandmark(landmarkA);
//        request.setMember(memberA);
//        request.setPoint(1000);
//        requests.add(request);
//
//        noticeService.notifyBidFailed(requests);
//
//        final Slice<Notice> resultA = noticeService.findByMemberId(memberA.getId(), Pageable.ofSize(20));
//
//        Assertions.assertThat(resultA.getContent().get(0).getContent()).contains("포인트가 지급되었습니다.");
//    }
//
//    @Test
//    @DisplayName("점령 시작 알림")
//    void notifyOccupationStart() {
//        List<NoticeRequestDto> requests = new ArrayList<>();
//
//        final NoticeRequestDto request = new NoticeRequestDto();
//
//        final Member memberA = Member.builder()
//                                     .id(2L)
//                                     .noticeToken("member_A_Token")
//                                     .email("memberA_email")
//                                     .nickname("memberA")
//                                     .build();
//
//        final Landmark landmarkA = Landmark.builder().name("Landmark A").build();
//
//        request.setLandmark(landmarkA);
//        request.setMember(memberA);
//        request.setPoint(1000);
//        requests.add(request);
//
//        noticeService.notifyOccupationStart(requests);
//
//        final Slice<Notice> resultA = noticeService.findByMemberId(memberA.getId(), Pageable.ofSize(20));
//
//        Assertions.assertThat(resultA.getContent().get(0).getContent()).contains("일주일간 점령하게 되었습니다.");
//    }
//
//    @Test
//    @DisplayName("점령 종료 알림")
//    void notifyOccupationPeriodEnd() {
//        List<NoticeRequestDto> requests = new ArrayList<>();
//
//        final NoticeRequestDto request = new NoticeRequestDto();
//
//        final Member memberA = Member.builder()
//                                     .id(2L)
//                                     .noticeToken("member_A_Token")
//                                     .email("memberA_email")
//                                     .nickname("memberA")
//                                     .build();
//
//        final Landmark landmarkA = Landmark.builder().name("Landmark A").build();
//
//        request.setLandmark(landmarkA);
//        request.setMember(memberA);
//        request.setPoint(1000);
//        requests.add(request);
//
//        noticeService.notifyOccupationPeriodEnd(requests);
//
//        final Slice<Notice> resultA = noticeService.findByMemberId(memberA.getId(), Pageable.ofSize(20));
//
//        Assertions.assertThat(resultA.getContent().get(0).getContent()).contains("종료되었습니다.");
//    }
//
//}