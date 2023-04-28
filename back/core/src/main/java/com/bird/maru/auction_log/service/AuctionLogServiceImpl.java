package com.bird.maru.auction_log.service;

import com.bird.maru.auction.repository.AuctionRepository;
import com.bird.maru.auction_log.repository.AuctionLogRepository;
import com.bird.maru.common.exception.ResourceNotFoundException;
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
        Member user = memberRepository.findById(member.getId()).orElseThrow(
                () -> new ResourceNotFoundException("해당 리소스 존재하지 않습니다.")
        );

        // 2. 현재 auction 테이블의 최고 입찰값 (없을 수도 있음) 가져오기
        auctionRepository.findByLandmarkAndNotFinished(landmarkId)
                         .ifPresentOrElse(
                                 auction -> { // 기존 최고 입찰 기록 있음
                                     int cost = auctionLogRepository.findById(auction.getLastLogId()).orElseThrow(
                                             () -> new ResourceNotFoundException("해당 리소스 존재하지 않습니다.")
                                     ).getPrice();

                                     if (price > cost && user.getPoint() >= price) { // 입찰 가격이 더 높은지
                                         // 4. auctionLog에 입찰 정보 등록
                                         AuctionLog newAuctionLog = createAuctionLog(auction, user, price);
                                         auctionLogRepository.save(newAuctionLog);

                                         // auction 테이블 갱신
                                         auction.changeLastLogId(newAuctionLog.getId());

                                         // 5. Member의 point 깎기
                                         user.bidPoint(price);

                                         // websocket 통신
                                     } else {
                                         // 포인트 부족 예외 처리
                                     }
                                 },
                                 () -> { // 최초 입찰
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
                         );
    }

    /**
     * 입찰 처리 (기존 입찰자) 경매 로그 업데이트
     *
     * @Param 랜드마크 PK, 입찰 가격
     */
    @Override
    public void auctionsReBidding(CustomUserDetails member, Long landmarkId, int price) {
        Member user = memberRepository.findById(member.getId()).orElseThrow(
                () -> new ResourceNotFoundException("해당 리소스 존재하지 않습니다.")
        );

        // 1. 현재 auctionLog에 입찰 기록이 있는지 확인
        auctionLogRepository.findByLandmarkAndMember(landmarkId, user.getId())
                            .ifPresentOrElse(
                                    auctionLog -> { // auctionLog에 입찰 기록이 있음 : 재입찰자
                                        auctionRepository.findByLandmarkAndNotFinished(landmarkId)
                                                         .ifPresentOrElse(
                                                                 auction -> { // auction 테이블에 최고 입찰가 있음
                                                                     int cost = auctionLogRepository.findById(auction.getLastLogId()).orElseThrow(
                                                                             () -> new ResourceNotFoundException("해당 리소스 존재하지 않습니다.")
                                                                     ).getPrice();

                                                                     if (price > cost && (user.getPoint() + auctionLog.getPrice()) >= price) {
                                                                         // 입찰 기록 업데이트
                                                                         auctionLog.bidding(price);

                                                                         //  Member의 point 깎기
                                                                         user.bidPoint(price - auctionLog.getPrice());

                                                                         // auction 테이블 갱신
                                                                         auction.changeLastLogId(user.getId());

                                                                         // websocket 통신
                                                                     } else {
                                                                         // 포인트 부족 예외 처리
                                                                     }
                                                                 },
                                                                 () -> { // auction 테이블에 최고 입찰가 없음
                                                                     // 입찰 기록 업데이트
                                                                     auctionLog.bidding(price);

                                                                     //  Member의 point 깎기
                                                                     user.bidPoint(price - auctionLog.getPrice());

                                                                     // auction 테이블 생성
                                                                     Auction newAuction = createAuction(
                                                                             landmarkRepository.getReferenceById(landmarkId));
                                                                     auctionRepository.save(newAuction);
                                                                     newAuction.changeLastLogId(user.getId());

                                                                     // websocket 통신
                                                                 }
                                                         );
                                    },
                                    () -> {
                                        throw new ResourceNotFoundException("해당 리소스 존재하지 않습니다.");
                                    }
                            );

    }

    /**
     * 경매 로그 삭제
     */
    @Override
    public void auctionsCancelBidding(CustomUserDetails member, Long auctionLogId) {
        Member user = memberRepository.findById(member.getId()).orElseThrow(
                () -> new ResourceNotFoundException("해당 리소스 존재하지 않습니다.")
        );

        AuctionLog auctionLog = auctionLogRepository.findById(auctionLogId).orElseThrow(
                () -> new ResourceNotFoundException("해당 리소스 존재하지 않습니다.")
        );

        auctionRepository.findByLandmarkAndNotFinished(auctionLog.getAuction().getLandmark().getId())
                         .ifPresentOrElse(
                                 auction -> {
                                     if (auction.getLastLogId().equals(auctionLog.getId())) {
                                         // 포인트 환불
                                         user.gainPoint(auctionLog.getPrice());

                                         // auctionLog 삭제
                                         auctionLogRepository.deleteById(auctionLogId);

                                         // auction 테이블 갱신
                                         // 하나의 입찰 건수만 있었을 수 있으므로 Optional 처리
                                         Optional<AuctionLog> maxAuctionLog = auctionLogRepository.findFirstNByLandmarkId(
                                                 auction.getLandmark().getId());

                                         if (maxAuctionLog.isPresent()) {
                                             auction.changeLastLogId(maxAuctionLog.get().getId());
                                         }

                                         // websocket 통신

                                     } else {
                                         // 포인트 환불
                                         user.gainPoint(auctionLog.getPrice());

                                         // auctionLog 삭제
                                         auctionLogRepository.deleteById(auctionLogId);
                                     }
                                 },
                                 () -> {
                                     // 포인트 환불
                                     user.gainPoint(auctionLog.getPrice());

                                     // auctionLog 삭제
                                     auctionLogRepository.deleteById(auctionLogId);
                                 }
                         );


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

    private AuctionLog createAuctionLog(Auction auction, Member member, int price) {
        return AuctionLog.builder()
                         .auction(auction)
                         .member(member)
                         .price(price)
                         .createdDateTime(LocalDateTime.now())
                         .build();
    }

    private Auction createAuction(Landmark landmark) {
        return Auction.builder()
                      .createdDate(LocalDate.now())
                      .landmark(landmark)
                      .finished(false)
                      .build();
    }

}
