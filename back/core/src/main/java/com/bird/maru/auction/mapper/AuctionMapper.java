package com.bird.maru.auction.mapper;

import com.bird.maru.auctionlog.controller.dto.LandmarkSimpleDto;
import com.bird.maru.domain.model.entity.Auction;
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

}
