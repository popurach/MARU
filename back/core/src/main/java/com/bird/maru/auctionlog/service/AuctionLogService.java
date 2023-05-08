package com.bird.maru.auctionlog.service;

import com.bird.maru.domain.model.entity.AuctionLog;
import java.util.List;

public interface AuctionLogService {

    void auctionsBidding(Long memberId, Long landmarkId, int price);

    void auctionsReBidding(Long memberId, Long landmarkId, int price);

    void auctionsCancelBidding(Long memberId, Long auctionLogId);

    void auctionExecute(AuctionLog auctionLogs);

    List<Integer> auctionRecord(Long landmarkId);

    Integer auctionBestPrice(Long landmarkId);

}
