package com.bird.maru.AuctionLog.repository;

import com.bird.maru.domain.model.entity.AuctionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionLogRepository extends JpaRepository<AuctionLog, Long> {

}
