package com.bird.maru.auction_log.mapper;

import com.bird.maru.auction_log.controller.dto.AuctionLogResponseDto;
import com.bird.maru.domain.model.entity.AuctionLog;
import java.util.List;
import java.util.stream.Collectors;

public class AuctionLogMapper {
    public static AuctionLogResponseDto toAuctionResponseDto(AuctionLog auctionLog) {
        return AuctionLogResponseDto.builder()
                                    .localDate(auctionLog.getAuction().getCreatedDate())
                                    .price(auctionLog.getPrice())
                                    .build();
    }

    public static List<AuctionLogResponseDto> toAuctionResponseDto(List<AuctionLog> auctionLogs) {
        return auctionLogs.stream().map(
                l -> toAuctionResponseDto(l)
        ).collect(Collectors.toList());
    }

}
