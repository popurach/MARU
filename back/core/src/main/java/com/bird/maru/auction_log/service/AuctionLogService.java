package com.bird.maru.auction_log.service;

import com.bird.maru.auction_log.controller.dto.AuctionLogResponseDto;
import com.bird.maru.domain.model.entity.AuctionLog;
import java.util.List;

public interface AuctionLogService {

    void auctionsBidding(Long memberId, Long landmarkId, int price);

    void auctionsReBidding(Long memberId, Long landmarkId, int price);

    void auctionsCancelBidding(Long memberId, Long auctionLogId);

    void auctionExecute(AuctionLog auctionLogs);

    List<AuctionLogResponseDto> auctionRecord(Long landmarkId);

}
