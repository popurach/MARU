package com.bird.maru.auction_log.repository;

import com.bird.maru.domain.model.entity.Auction;
import com.bird.maru.domain.model.entity.AuctionLog;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionLogRepository extends JpaRepository<AuctionLog, Long> {

    @Query("SELECT al "
            + "FROM AuctionLog al "
            + "JOIN FETCH al.member "
            + "JOIN FETCH al.auction a "
            + "JOIN FETCH a.landmark ")
    Optional<AuctionLog> findAllWithAuctionAndMember();

    @Query("SELECT al "
            + "FROM AuctionLog al "
            + "WHERE al.member.id = :memberId AND al.auction.landmark.id = :landmarkId")
    Optional<AuctionLog> findByLandmarkAndMember(@Param("memberId") Long memberId, @Param("landmarkId") Long landmarkId);

    @Query("SELECT al "
            + "FROM AuctionLog al "
            + "WHERE al.auction.landmark.id = :landmarkId "
            + "AND al.price = "
                    + "(SELECT MAX(al2.price) "
                    + "FROM AuctionLog al2 "
                    + "WHERE al2.auction.landmark.id = :landmarkId)")
    Optional<AuctionLog> findFirstByLandmarkId(@Param("landmarkId") Long landmarkId);

    @Query("SELECT al "
            + "FROM AuctionLog al "
            + "INNER JOIN Auction a ON al.id = a.lastLogId "
            + "WHERE a.landmark.id = :landmarkId AND a.finished = true "
            + "ORDER BY a.createdDate")
    List<AuctionLog> auctionRecord(@Param("landmarkId") Long landmarkId, Pageable pageable);
}
