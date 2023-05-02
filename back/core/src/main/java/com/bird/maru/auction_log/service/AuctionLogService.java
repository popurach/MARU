package com.bird.maru.auction_log.service;

import com.bird.maru.domain.model.entity.AuctionLog;

public interface AuctionLogService {

    void auctionsBidding(Long memberId, java.lang.Long landmarkId, int price);

    void auctionsReBidding(Long memberId, java.lang.Long landmarkId, int price);

    void auctionsCancelBidding(Long memberId, java.lang.Long auctionLogId);

    void auctionExecute(AuctionLog auctionLogs);

}
