package com.bird.maru.auctionlog.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 경매 낙찰가 기록 반환용 DTO입니다
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class AuctionLogResponseDto {
    private Integer price;

}
