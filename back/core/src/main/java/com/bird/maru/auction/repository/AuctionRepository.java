package com.bird.maru.auction.repository;

import com.bird.maru.domain.model.entity.Auction;
import com.bird.maru.domain.model.entity.Landmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {
    Auction findByLandmarkIdAndFinished(Long landmarkId, Boolean finished);
}
