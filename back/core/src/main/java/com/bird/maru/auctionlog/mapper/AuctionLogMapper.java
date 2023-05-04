package com.bird.maru.auctionlog.mapper;

import com.bird.maru.auctionlog.controller.dto.AuctionLogResponseDto;
import com.bird.maru.auctionlog.controller.dto.AuctionLogSimpleDto;
import com.bird.maru.auctionlog.controller.dto.LandmarkSimpleDto;
import com.bird.maru.domain.model.entity.AuctionLog;
import com.bird.maru.domain.model.entity.Landmark;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuctionLogMapper {

    public static AuctionLogResponseDto toAuctionResponseDto(AuctionLog auctionLog) {
        return AuctionLogResponseDto.builder()
                                    .price(auctionLog.getPrice())
                                    .build();
    }

    public static List<AuctionLogResponseDto> toAuctionResponseDto(List<AuctionLog> auctionLogs) {
        return auctionLogs.stream().map(
                l -> toAuctionResponseDto(l)
        ).collect(Collectors.toList());
    }

    public static AuctionLogSimpleDto toAuctionLogSimpleDto(AuctionLog auctionLog) {
        Landmark landmark = auctionLog.getAuction().getLandmark();
        return AuctionLogSimpleDto.builder()
                                  .id(auctionLog.getId())
                                  .landmark(
                                          LandmarkSimpleDto.builder()
                                                           .id(landmark.getId())
                                                           .name(landmark.getName())
                                                           .build()
                                  )
                                  .build();
    }

    public static List<AuctionLogSimpleDto> toAuctionLogSimpleDto(List<AuctionLog> auctionLogs) {
        return auctionLogs.stream()
                          .map(AuctionLogMapper::toAuctionLogSimpleDto)
                          .collect(Collectors.toList());
    }

}
