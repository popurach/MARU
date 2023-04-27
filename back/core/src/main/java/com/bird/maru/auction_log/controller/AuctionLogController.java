package com.bird.maru.auction_log.controller;

import com.bird.maru.auction_log.service.AuctionLogService;
import com.bird.maru.domain.model.type.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auction")
@Slf4j
public class AuctionLogController {

    private final AuctionLogService auctionLogService;

    /**
     * 경매 입찰 처리 (LandmarkId, 입찰 금액) / 입찰 금액 유효성 체크 필요
     */
    @PostMapping("/bidding")
    public void auctionsBidding(@AuthenticationPrincipal CustomUserDetails member) {
        auctionLogService.auctionsBidding();
    }

    /**
     * 경매 재 입찰 처리 (AuctionLogId, 입찰 금액)/ 입찰 금액 유효성 체크 필요
     */
    @PutMapping("/bidding")
    public void auctionsReBidding() {

    }

    /**
     * 경매 입찰 취소 처리 (AuctionLogId)
     */
    @DeleteMapping("/bidding")
    public void auctionsCancelBidding() {

    }

    /**
     * 해당 랜드마크 경매 정보 조회
     */
    @GetMapping
    public void searchLandmarkById() {

    }

}
