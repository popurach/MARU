package com.bird.maru.auctionlog.repository.query;

import com.bird.maru.domain.model.entity.AuctionLog;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionLogQueryRepository extends JpaRepository<AuctionLog, Long> {

    @Query("SELECT al FROM AuctionLog al "
            + "JOIN al.auction a ON a.finished = FALSE "
            + "WHERE al.member.id = :memberId "
            + "AND a.landmark.id = :landmarkId ")
    Optional<AuctionLog> findByMemberAndLandmark(@Param("memberId") Long memberId, @Param("landmarkId") Long landmarkId);

}
