package com.bird.maru.auction_log.controller.dto;

import java.time.LocalDate;
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

    private LocalDate localDate;
    private Integer price;

}
