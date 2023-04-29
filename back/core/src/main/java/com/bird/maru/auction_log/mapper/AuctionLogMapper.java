package com.bird.maru.auction_log.mapper;

import com.bird.maru.auction_log.controller.dto.AuctionLogResponseDto;
import com.bird.maru.domain.model.entity.AuctionLog;

public class AuctionLogMapper {
    public static AuctionLogResponseDto toAuctionResponseDto(AuctionLog auctionLog) {
        return AuctionLogResponseDto.builder()
                                    .localDate(auctionLog.getAuction().getCreatedDate())
                                    .price(auctionLog.getPrice())
                                    .build();
    }
}
