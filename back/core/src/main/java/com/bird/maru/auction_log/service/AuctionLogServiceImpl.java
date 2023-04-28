package com.bird.maru.auction_log.service;

import com.bird.maru.auction.repository.AuctionRepository;
import com.bird.maru.auction_log.repository.AuctionLogRepository;
import com.bird.maru.domain.model.entity.Auction;
import com.bird.maru.domain.model.entity.AuctionLog;
import com.bird.maru.domain.model.entity.Landmark;
import com.bird.maru.domain.model.entity.Member;
import com.bird.maru.domain.model.type.CustomUserDetails;
import com.bird.maru.landmark.repository.LandmarkRepository;
import com.bird.maru.member.repository.MemberRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
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

    private final AuctionLogRepository auctionLogRepository;
    private final AuctionRepository auctionRepository;
    private final MemberRepository memberRepository;
    private final LandmarkRepository landmarkRepository;

    /**
     * 입찰 처리 (신규 입찰자)
     *
     * @Param member OAuth2 인증에 성공한 회원
     * @Param 랜드마크 PK, 입찰 가격
     */
    @Override
    public void auctionsBidding(CustomUserDetails member, Long landmarkId, int price) {
        Member user = memberRepository.getReferenceById(member.getId());

        // 1. 현재 auctionLog에 입찰 기록이 있는지 확인
        Optional<AuctionLog> auctionLog = auctionLogRepository.findByLandmarkAndMember(landmarkId, member.getId());

        // 2. 현재 auction 테이블의 최고 입찰값 (없을 수도 있음) 가져오기
        Optional<Auction> auction = auctionRepository.findByLandmarkAndFinished(landmarkId);

        // 3. auction 등록
        if (auction.isPresent()) { // 기존 입찰 기록 있음
            int cost = auctionLogRepository.findById(auction.get().getLastLogId()).get().getPrice();
            if (price > cost && user.getPoint() >= price) { // 입찰 가격이 더 높은지
                // 4. auctionLog에 입찰 정보 등록
                AuctionLog newAuctionLog = createAuctionLog(auction.get(), user, price);
                auctionLogRepository.save(newAuctionLog);

                // auction 테이블 갱신
                auction.get().changeLastLogId(newAuctionLog.getId());

                // 5. Member의 point 깎기
                user.bidPoint(price);

                // websocket 통신

            } else {
                // 예외처리
            }
        } else { // 최초 입찰자
            // auction 생성
            Auction newAuction = createAuction(landmarkRepository.getReferenceById(landmarkId));
            auctionRepository.save(newAuction);

            // auctionLog 생성
            AuctionLog newAuctionLog = createAuctionLog(newAuction, user, price);
            auctionLogRepository.save(newAuctionLog);
            newAuction.setLastLogId(newAuctionLog.getId());

            // Member의 point 깎기
            user.bidPoint(price);

            // websocket 통신

        }

    }

    /**
     * 입찰 처리 (기존 입찰자) 경매 로그 업데이트
     */
    @Override
    public void auctionsReBidding(CustomUserDetails member, Long landmarkId, int price) {
        Member user = memberRepository.getReferenceById(member.getId());

        // 1. 현재 auctionLog에 입찰 기록이 있는지 확인
        AuctionLog auctionLog = auctionLogRepository.findByLandmarkAndMember(landmarkId, user.getId()).orElseThrow();

        // 2. 현재 auction 테이블의 최고 입찰값 (없을 수도 있음) 가져오기
        Auction auction = auctionRepository.findByLandmarkAndFinished(landmarkId).orElseThrow();

        int cost = auctionLogRepository.findById(auction.getLastLogId()).get().getPrice();

        if (price > cost && (user.getPoint() + auctionLog.getPrice()) >= price) {
            // 입찰 기록 업데이트
            auctionLog.bidding(price);

            //  Member의 point 깎기
            user.bidPoint(price - auctionLog.getPrice());

            // auction 테이블 갱신
            auction.changeLastLogId(user.getId());

            // websocket 통신
        } else {
            // 예외 처리
        }
    }

    /**
     * 경매 로그 삭제
     */
    @Override
    public void auctionsCancelBidding(CustomUserDetails member, Long auctionLogId) {
        Member user = memberRepository.getReferenceById(member.getId());

        AuctionLog auctionLog = auctionLogRepository.findById(auctionLogId).orElseThrow();

        // 포인트 환불
        user.gainPoint(auctionLog.getPrice());

        // auctionLog 삭제
        auctionLogRepository.deleteById(auctionLogId);
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

    public AuctionLog createAuctionLog(Auction auction, Member member, int price) {
        return AuctionLog.builder()
                         .auction(auction)
                         .member(member)
                         .price(price)
                         .createdDateTime(LocalDateTime.now())
                         .build();
    }

    public Auction createAuction(Landmark landmark) {
        return Auction.builder()
                      .createdDate(LocalDate.now())
                      .landmark(landmark)
                      .finished(false)
                      .build();
    }

}
