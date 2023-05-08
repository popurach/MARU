package com.bird.maru.auction.repository;

import com.bird.maru.domain.model.entity.Auction;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {

    @Query("SELECT a FROM Auction a WHERE a.landmark.id = :landmarkId AND a.finished = :finished")
    Optional<Auction> findByLandmarkAndFinished(@Param("landmarkId") Long landmarkId, @Param("finished") Boolean finished);

    Page<Auction> findByFinished(Boolean finished, Pageable pageable);
}
