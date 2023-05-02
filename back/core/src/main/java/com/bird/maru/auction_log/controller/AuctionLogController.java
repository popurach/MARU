package com.bird.maru.auction_log.controller;

import com.bird.maru.auction_log.controller.dto.AuctionLogRequestDto;
import com.bird.maru.auction_log.service.AuctionLogService;
import com.bird.maru.auth.service.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auctions")
@Slf4j
public class AuctionLogController {

    private final AuctionLogService auctionLogService;

    /**
     * 경매 입찰 처리 (LandmarkId, 입찰 금액) / 입찰 금액 유효성 체크 필요
     *
     * @Param auctionLogRequestDto [landmarkId, price]
     */
    @PostMapping("/bidding")
    public void auctionsBidding(@AuthenticationPrincipal CustomUserDetails member, @RequestBody AuctionLogRequestDto auctionLogRequestDto) {
        auctionLogService.auctionsBidding(member.getId(), auctionLogRequestDto.getLandmarkId(), auctionLogRequestDto.getPrice());
    }

    /**
     * 경매 재입찰 처리 (AuctionLogId, 입찰 금액)/ 입찰 금액 유효성 체크 필요
     *
     * @Param auctionLogRequestDto [landmarkId, price]
     */
    @PutMapping("/bidding")
    public void auctionsReBidding(@AuthenticationPrincipal CustomUserDetails member, @RequestBody AuctionLogRequestDto auctionLogRequestDto) {
        auctionLogService.auctionsReBidding(member.getId(), auctionLogRequestDto.getLandmarkId(), auctionLogRequestDto.getPrice());
    }

    /**
     * 경매 입찰 취소 처리 (AuctionLogId)
     *
     * @Param id : auctionLogId (해당 경매 로그 테이블의 PK)
     */
    @DeleteMapping("/bidding/{id}")
    public void auctionsCancelBidding(@AuthenticationPrincipal CustomUserDetails member, @PathVariable Long id) {
        auctionLogService.auctionsCancelBidding(member.getId(), id);
    }

    /**
     * 해당 랜드마크 경매 정보 조회
     */
    @GetMapping("/{id}")
    public void searchLandmarkById(@AuthenticationPrincipal CustomUserDetails member, @PathVariable Long id) {
        auctionLogService.auctionRecord(id);
    }

}
