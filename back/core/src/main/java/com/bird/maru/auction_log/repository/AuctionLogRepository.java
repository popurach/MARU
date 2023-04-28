package com.bird.maru.auction_log.repository;

import com.bird.maru.domain.model.entity.Auction;
import com.bird.maru.domain.model.entity.AuctionLog;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionLogRepository extends JpaRepository<AuctionLog, Long> {
    // 1. 현재 auctionLog에 입찰 기록이 있는지 확인

    // 2. 입찰 price 지불이 가능한지 확인

    // 3. 현재 auction 테이블의 값 (없을 수도 있음) 보다 price가 높은지 확인

    // 4. auctionLog에 입찰 정보 등록

    // 5. Member의 point 깎기

    @Query("SELECT al "
            + "FROM AuctionLog al "
            + "JOIN FETCH al.member "
            + "JOIN FETCH al.auction a "
            + "JOIN FETCH a.landmark ")
    Optional<AuctionLog> findAllWithAuctionAndMember();

    @Query("SELECT al FROM AuctionLog al WHERE al.member.id = :memberId AND al.auction.landmark.id = :landmarkId")
    Optional<AuctionLog> findByLandmarkAndMember(@Param("memberId") Long memberId, @Param("landmarkId") Long landmarkId);

    @Query("SELECT al FROM AuctionLog al WHERE al.auction.landmark.id = :landmarkId AND al.price = (SELECT MAX(al2.price) FROM AuctionLog al2 WHERE al2.auction.landmark.id = :landmarkId)")
    Optional<AuctionLog> findFirstNByLandmarkId(@Param("landmarkId") Long landmarkId);

}
