package com.bird.maru.auction.repository;

import com.bird.maru.domain.model.entity.Auction;
import com.bird.maru.domain.model.entity.Landmark;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {

    Auction findByLandmarkIdAndFinished(Long landmarkId, Boolean finished);

    @Query("SELECT a FROM Auction a WHERE a.landmark.id = :landmarkId AND a.finished = false")
    Optional<Auction> findByLandmarkAndFinished(@Param("landmarkId") Long landmarkId);

}
