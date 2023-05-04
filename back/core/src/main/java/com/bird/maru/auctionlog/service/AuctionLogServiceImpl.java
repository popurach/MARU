package com.bird.maru.auctionlog.service;

import com.bird.maru.auction.repository.AuctionRepository;
import com.bird.maru.auctionlog.repository.AuctionLogRepository;
import com.bird.maru.auctionlog.repository.query.AuctionLogCustomQueryRepository;
import com.bird.maru.common.config.NamedLockConfig;
import com.bird.maru.common.exception.NotEnoughMoney;
import com.bird.maru.common.exception.ResourceNotFoundException;
import com.bird.maru.domain.model.entity.Auction;
import com.bird.maru.domain.model.entity.AuctionLog;
import com.bird.maru.domain.model.entity.Landmark;
import com.bird.maru.domain.model.entity.Member;
import com.bird.maru.landmark.repository.LandmarkRepository;
import com.bird.maru.member.repository.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuctionLogServiceImpl implements AuctionLogService {

    private final NamedLockConfig namedLockConfig;
    private final AuctionLogRepository auctionLogRepository;
    private final AuctionLogCustomQueryRepository auctionLogCustomQueryRepository;
    private final AuctionRepository auctionRepository;
    private final MemberRepository memberRepository;

    /**
     * 입찰 처리 (신규 입찰자)
     *
     * @Param member OAuth2 인증에 성공한 회원
     * @Param 랜드마크 PK, 입찰 가격
     */
    @Override
    public void auctionsBidding(Long memberId, Long landmarkId, int price) {
        Member member = memberRepository.findById(memberId)
                                        .orElseThrow(() -> new ResourceNotFoundException("해당 리소스 존재하지 않습니다."));

        if (member.getPoint() < price) {
            throw new NotEnoughMoney("포인트가 부족합니다.");
        }

        // 2. 현재 auction 테이블의 최고 입찰값 (없을 수도 있음) 가져오기
        Auction auction = auctionRepository.findByLandmarkAndFinished(landmarkId, Boolean.FALSE)
                                           .orElseThrow(() -> new ResourceNotFoundException("해당 리소스 존재하지 않습니다."));
        biddingWithAuction(auction, member, price);
    }

    /**
     * 입찰 처리 (기존 입찰자) 경매 로그 업데이트
     *
     * @Param 랜드마크 PK, 입찰 가격
     */
    @Override
    public void auctionsReBidding(Long memberId, Long landmarkId, int price) {
        Member member = memberRepository.findById(memberId)
                                        .orElseThrow(() -> new ResourceNotFoundException("해당 리소스 존재하지 않습니다."));

        // 1. 현재 auctionLog에 입찰 기록이 있는지 확인
        AuctionLog auctionLog = auctionLogCustomQueryRepository.findByLandmarkAndMember(memberId, landmarkId)
                                                               .orElseThrow(() -> new ResourceNotFoundException("해당 리소스 존재하지 않습니다."));

        if ((member.getPoint() + auctionLog.getPrice()) < price) {
            throw new NotEnoughMoney("포인트가 부족합니다.");
        }

        Auction auction = auctionRepository.findByLandmarkAndFinished(landmarkId, Boolean.FALSE)
                                           .orElseThrow(() -> new ResourceNotFoundException("해당 리소스 존재하지 않습니다."));

        int prevCost = auctionLogRepository.findById(auction.getLastLogId())
                                           .orElseThrow(() -> new ResourceNotFoundException("해당 리소스 존재하지 않습니다."))
                                           .getPrice();

        if (price <= prevCost) {
            throw new NotEnoughMoney("입찰 금액이 최고가보다 낮습니다.");
        }

        //  Member의 point 깎기
        member.bidPoint(price - auctionLog.getPrice());

        // 입찰 기록 업데이트
        auctionLog.bidding(price);

        // auction 테이블 갱신
        auction.changeLastLogId(auctionLog.getId());

        // websocket 통신

    }

    /**
     * 경매 로그 삭제
     */
    @Override
    public void auctionsCancelBidding(Long memberId, Long auctionLogId) {
        Member member = memberRepository.findById(memberId)
                                        .orElseThrow(() -> new ResourceNotFoundException("해당 리소스 존재하지 않습니다."));

        AuctionLog auctionLog = auctionLogCustomQueryRepository.findWithAuctionById(auctionLogId)
                                                               .orElseThrow(() -> new ResourceNotFoundException("해당 리소스 존재하지 않습니다."));

        // 포인트 돌려주기
        member.gainPoint(auctionLog.getPrice());
        auctionLogRepository.delete(auctionLog);

        // 최고 입찰자가 아닌 경우
        if (!auctionLogId.equals(auctionLog.getAuction().getLastLogId())) {
            return;
        }

        Auction auction = auctionLog.getAuction();
        auctionLogRepository.findFirstByLandmarkId(auction.getLandmark().getId())
                            .ifPresentOrElse(
                                    log -> auction.setLastLogId(log.getId()),
                                    () -> auction.changeLastLogId(null)
                            );

        // websocket 통신

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

        if (successfulBidId.equals(auctionLog.getId())) { // 낙찰자
            log.info("낙찰자 : {}", member.getId());
            landmark.changeOwner(member.getId()); // 랜드마크 대표 회원 정보 업데이트
            auction.setFinished(true); // 경매 현재 상태 -> 끝남
        } else { // 유찰자
            int price = auctionLog.getPrice(); // 유찰자의 입찰 가격
            member.gainPoint(price);
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
            return new ArrayList<>();
        }
        List<Integer> auctionRecords = auctionLogList.stream().map(
                auctionLog -> auctionLog.getPrice() != null ? auctionLog.getPrice() : 0
        ).collect(Collectors.toList());
        return auctionRecords;
    }

    private void biddingWithAuction(Auction auction, Member member, int price) {
        log.info("auction 정보 : {}, {}", auction.getCreatedDate(), auction.getLandmark().getId());
        log.info("member 정보 : {}", member.getNickname());
        log.info("입찰 가격 : {}", price);

        if (auction.getLastLogId() != null) {
            int prevCost = auctionLogRepository.findById(auction.getLastLogId())
                                               .orElseThrow(() -> new ResourceNotFoundException("해당 리소스 존재하지 않습니다."))
                                               .getPrice();
            if (price < prevCost) {
                throw new NotEnoughMoney("입찰 금액이 최고가보다 낮습니다.");
            }
        }

        // 입찰 가격이 더 높은 경우
        // 4. auctionLog에 입찰 정보 등록
        AuctionLog newAuctionLog = createAuctionLog(auction, member, price);
        auctionLogRepository.save(newAuctionLog);

        // auction 테이블 갱신
        auction.changeLastLogId(newAuctionLog.getId());

        // 5. Member의 point 깎기
        member.bidPoint(price);

        // websocket 통신
    }

    private AuctionLog createAuctionLog(Auction auction, Member member, int price) {
        return AuctionLog.builder()
                         .auction(auction)
                         .member(member)
                         .price(price)
                         .build();
    }

}
