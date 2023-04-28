package com.bird.maru.auction_log.service;

import com.bird.maru.domain.model.entity.AuctionLog;
import com.bird.maru.domain.model.type.CustomUserDetails;

public interface AuctionLogService {

    void auctionsBidding(CustomUserDetails member, Long landmarkId, int price);

    void auctionsReBidding(CustomUserDetails member, Long landmarkId, int price);

    void auctionsCancelBidding(CustomUserDetails member, Long auctionLogId);

    void auctionExecute(AuctionLog auctionLogs);

}
