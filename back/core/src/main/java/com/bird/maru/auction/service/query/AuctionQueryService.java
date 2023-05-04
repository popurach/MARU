package com.bird.maru.auction.service.query;

import com.bird.maru.auction.controller.dto.AuctionDetailDto;
import com.bird.maru.auction.controller.dto.AuctionSearchCondition;
import com.bird.maru.domain.model.entity.Auction;
import java.util.List;

public interface AuctionQueryService {

    List<Auction> findMyNonBiddingAuctions(Long memberId, AuctionSearchCondition condition);

    AuctionDetailDto findAuctionDetail(Long memberId, Long landmarkId);

}
