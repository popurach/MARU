package com.bird.maru.auctionlog.service;

import com.bird.maru.auction.repository.AuctionRepository;
import com.bird.maru.auctionlog.repository.AuctionLogRepository;
import com.bird.maru.auctionlog.repository.query.AuctionLogCustomQueryRepository;
import com.bird.maru.common.config.WebSocket.Bid;
import com.bird.maru.common.exception.NotEnoughMoneyException;
import com.bird.maru.common.exception.ResourceNotFoundException;
import com.bird.maru.domain.model.entity.Auction;
import com.bird.maru.domain.model.entity.AuctionLog;
import com.bird.maru.domain.model.entity.Landmark;
import com.bird.maru.domain.model.entity.Member;
import com.bird.maru.landmark.repository.LandmarkRepository;
import com.bird.maru.member.repository.MemberRepository;
import com.bird.maru.notice.model.NoticeRequestDto;
import com.bird.maru.notice.service.NoticeService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuctionLogServiceImpl implements AuctionLogService {

    private final NoticeService noticeService;
    private final SimpMessageSendingOperations messagingTemplate;
    private final AuctionLogRepository auctionLogRepository;
    private final AuctionLogCustomQueryRepository auctionLogCustomQueryRepository;
    private final AuctionRepository auctionRepository;
    private final MemberRepository memberRepository;
    private final LandmarkRepository landmarkRepository;

    /**
     * 입찰 처리 (신규 입찰, 재입찰)
     *
     * @Param memberId  OAuth2 인증에 성공한 회원
     * @Param landmarkId 랜드마크 PK
     * @Param price 입찰 가격
     */
    @Override
    public void auctionsBidding(Long memberId, Long landmarkId, int price) {
        Member member = memberRepository.findById(memberId)
                                        .orElseThrow(() -> new ResourceNotFoundException("해당 리소스 존재하지 않습니다."));

        Auction auction = auctionRepository.findByLandmarkAndFinished(landmarkId, Boolean.FALSE)
                                           .orElseThrow(() -> new ResourceNotFoundException("해당 리소스 존재하지 않습니다."));

        Landmark landmark = landmarkRepository.findById(landmarkId)
                                              .orElseThrow(() -> new ResourceNotFoundException("해당 리소스 존재하지 않습니다."));

        // 1. 현재 auctionLog 입찰 기록이 있는지 확인
        Optional<AuctionLog> auctionLog = auctionLogCustomQueryRepository.findByLandmarkAndMember(member.getId(), landmarkId);

        if (auctionLog.isEmpty()) { // 2-1. auctionLog 값이 없다면 신규 입찰자 - 포인트 넉넉 체크
            if (member.getPoint() < price) {
                throw new NotEnoughMoneyException("포인트가 부족합니다.");
            }
        } else { // 2-2. auctionLog 값이 있다면 재 입찰자 - 포인트 넉넉 체크
            if (member.getPoint() + auctionLog.get().getPrice() < price) {
                throw new NotEnoughMoneyException("포인트가 부족합니다.");
            }
        }

        if (auction.getLastLogId() != null) { // 기존 최고 입찰 기록 < 입찰가 넘는지 체크
            AuctionLog prevAuctionLog = auctionLogRepository.findWithMemberById(auction.getLastLogId())
                                                            .orElseThrow(() -> new ResourceNotFoundException("해당 리소스 존재하지 않습니다."));
//            AuctionLog prevAuctionLog = auctionLogRepository.findById(auction.getLastLogId())
//                                                            .orElseThrow(() -> new ResourceNotFoundException("해당 리소스 존재하지 않습니다."));
            int prevCost = prevAuctionLog.getPrice();
            if (price <= prevCost) {
                throw new NotEnoughMoneyException("입찰 금액이 최고가보다 낮습니다.");
            }
            // 상위 입찰자 알림 설정
            if (!prevAuctionLog.getMember().getId().equals(memberId)) {
                noticeService.notifyTopBidderRevoked(new NoticeRequestDto(prevAuctionLog.getMember(), landmark, price));
            }
        }

        // 포인트에 대한 조건은 모두 충족 시
        if (auctionLog.isEmpty()) { // 신규 입찰자
            // Member point 깎기
            member.bidPoint(price);

            // 4. auctionLog 입찰 정보 등록
            AuctionLog newAuctionLog = createAuctionLog(auction, member, price);
            auctionLogRepository.save(newAuctionLog);

            // auction 테이블 갱신
            auction.changeLastLogId(newAuctionLog.getId());

            // 인찰 성공 알림
            noticeService.notifyBidSuccessful(new NoticeRequestDto(member, landmark, price));
        } else { // 재 입찰자
            // Member point 깎기
            int totalPrice = price - auctionLog.get().getPrice();
            member.bidPoint(totalPrice);

            // 입찰 기록 업데이트
            auctionLog.get().bidding(price);

            // auction 테이블 갱신
            auction.changeLastLogId(auctionLog.get().getId());

            // 입찰 성공 알림
            noticeService.notifyBidSuccessful(new NoticeRequestDto(member, landmark, totalPrice));
        }
        // websocket 통신
        Bid updateBid = new Bid(price, auction.getLandmark().getId());
        messagingTemplate.convertAndSend("/bidding/price", updateBid);

    }

    /**
     * 경매 로그 삭제
     */
    @Override
    public void auctionsCancelBidding(Long memberId, Long auctionLogId) {
        Member member = memberRepository.findById(memberId)
                                        .orElseThrow(() -> new ResourceNotFoundException("해당 리소스 존재하지 않습니다."));

        AuctionLog auctionLog = auctionLogCustomQueryRepository.findWithAuctionById(memberId, auctionLogId)
                                                               .orElseThrow(() -> new ResourceNotFoundException("해당 리소스 존재하지 않습니다."));

        // 포인트 돌려주기
        member.gainPoint(auctionLog.getPrice());
        auctionLogRepository.delete(auctionLog);

        // 최고 입찰자가 아닌 경우
        if (!auctionLogId.equals(auctionLog.getAuction().getLastLogId())) {
            return;
        }

        Auction auction = auctionLog.getAuction();
        Optional<AuctionLog> auctionLogMax = auctionLogCustomQueryRepository.findFirstByLandmarkId(auction.getLandmark().getId());
        if (auctionLogMax.isPresent()) { // 최고 입찰 기록으로 update
            auction.changeLastLogId(auctionLogMax.get().getId());

            // websocket 통신
            Bid updateBid = new Bid(auctionLogMax.get().getPrice(), auction.getLandmark().getId());
            messagingTemplate.convertAndSend("/bidding/price", updateBid);
        } else { // 최고 입찰 기록 x -> NULL
            auction.changeLastLogId(null);

            // websocket 통신
            Bid updateBid = new Bid(10000, auction.getLandmark().getId());
            messagingTemplate.convertAndSend("/bidding/price", updateBid);
        }

    }

    @Transactional(
//            rollbackFor = Exception.class,
            propagation = Propagation.REQUIRES_NEW
    )
    @Override
    public void auctionExecute(AuctionLog auctionLog) {
        Member member = auctionLog.getMember();
        Auction auction = auctionLog.getAuction();
        Landmark landmark = auction.getLandmark();
        Long successfulBidId = auction.getLastLogId();

        if (Boolean.TRUE.equals(auction.getFinished())) {
            throw new ResourceNotFoundException("해당 리소스는 끝난 경매 기록입니다.");
        }
        if (successfulBidId != null && successfulBidId.equals(auctionLog.getId())) { // 낙찰자
            log.info("낙찰자 : {}", member.getId());
            landmark.changeOwner(member.getId()); // 랜드마크 대표 회원 정보 업데이트
            // 랜드마크 점령 시작 알림 (낙찰자)
            noticeService.notifyOccupationStart(new NoticeRequestDto(member, landmark, 0));
        } else { // 유찰자
            int price = auctionLog.getPrice(); // 유찰자의 입찰 가격
            member.gainPoint(price);
            // auctionLog 삭제
            auctionLogRepository.delete(auctionLog);
            // 유찰 알림
            noticeService.notifyBidFailed(new NoticeRequestDto(member, landmark, price));
        }
    }

    /**
     * 해당 랜드마크 관련 최신 낙찰 데이터 10개 반환
     *
     * @Param 랜드마크 PK
     */
    @Override
    public List<Integer> auctionRecord(Long landmarkId) {
        List<AuctionLog> auctionLogList = auctionLogCustomQueryRepository.auctionRecordTop10(landmarkId);
        if (auctionLogList.isEmpty()) {
            List<Integer> auctionRecords = new ArrayList<>();
            auctionRecords.add(10000);
            return auctionRecords;
        }
        List<Integer> auctionRecords = auctionLogList.stream().map(
                auctionLog -> auctionLog.getPrice() != null ? auctionLog.getPrice() : 0
        ).collect(Collectors.toList());
        auctionRecords.add(0, 10000);
        return auctionRecords;
    }

    /**
     * 해당 랜드마크 최고 입찰가를 반환
     *
     * @Param 랜드마크 PK
     */
    @Override
    public Integer auctionBestPrice(Long landmarkId) {
        Optional<AuctionLog> auctionLog = auctionLogCustomQueryRepository.findFirstByLandmarkId(landmarkId);
        if (auctionLog.isPresent()) {
            return auctionLog.get().getPrice();
        }
        return 10000;
    }


    private AuctionLog createAuctionLog(Auction auction, Member member, int price) {
        return AuctionLog.builder()
                         .auction(auction)
                         .member(member)
                         .price(price)
                         .build();
    }

}
