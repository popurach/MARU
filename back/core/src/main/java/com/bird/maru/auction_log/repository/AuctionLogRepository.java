package com.bird.maru.auction_log.repository;

import com.bird.maru.domain.model.entity.AuctionLog;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionLogRepository extends JpaRepository<AuctionLog, Long> {

    @Query("SELECT al "
            + "FROM AuctionLog al "
            + "JOIN FETCH al.member "
            + "JOIN FETCH al.auction a "
            + "JOIN FETCH a.landmark ")
    Optional<AuctionLog> findAllWithAuctionAndMember();

}
