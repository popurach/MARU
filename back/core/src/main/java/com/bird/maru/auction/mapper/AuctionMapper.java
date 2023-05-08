package com.bird.maru.auction.mapper;

import com.bird.maru.auction.controller.dto.AuctionDetailDto;
import com.bird.maru.auction.controller.dto.MyAuctionLogDto;
import com.bird.maru.auctionlog.controller.dto.LandmarkSimpleDto;
import com.bird.maru.domain.model.entity.Auction;
import com.bird.maru.domain.model.entity.AuctionLog;
import com.bird.maru.domain.model.entity.Landmark;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuctionMapper {

    public static LandmarkSimpleDto toLandmarkSimpleDto(Auction auction) {
        Landmark landmark = auction.getLandmark();
        return LandmarkSimpleDto.builder()
                                .id(landmark.getId())
                                .name(landmark.getName())
                                .build();
    }

    public static List<LandmarkSimpleDto> toLandmarkSimpleDto(List<Auction> auction) {
        return auction.stream()
                      .map(AuctionMapper::toLandmarkSimpleDto)
                      .collect(Collectors.toList());
    }

    public static AuctionDetailDto toAuctionDetailDto(Landmark landmark, AuctionLog auctionLog) {
        return AuctionDetailDto.builder()
                               .name(landmark.getName())
                               .myBidding(
                                       MyAuctionLogDto.builder()
                                                      .id(auctionLog.getId())
                                                      .price(auctionLog.getPrice())
                                                      .build()
                               )
                               .build();
    }

    public static AuctionDetailDto toAuctionDetailDto(Landmark landmark) {
        return AuctionDetailDto.builder()
                               .name(landmark.getName())
                               .build();
    }

}
