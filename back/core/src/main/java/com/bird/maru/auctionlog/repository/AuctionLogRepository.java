package com.bird.maru.auctionlog.repository;

import com.bird.maru.domain.model.entity.AuctionLog;
import java.util.Optional;
import org.springframework.data.domain.Page;
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

    Page<AuctionLog> findAllByAuction_Finished(Boolean finished, Pageable pageable);

    @Query("SELECT al "
            + "FROM AuctionLog al "
            + "JOIN FETCH al.auction "
            + "WHERE al.id = :auctionLogId")
    Optional<AuctionLog> findWithMemberById(@Param("auctionLogId") Long auctionLogId);
}
