package com.bird.maru.auction.controller;

import com.bird.maru.auction.controller.dto.AuctionSearchCondition;
import com.bird.maru.auction.mapper.AuctionMapper;
import com.bird.maru.auction.service.query.AuctionQueryService;
import com.bird.maru.auctionlog.controller.dto.LandmarkSimpleDto;
import com.bird.maru.auth.service.dto.CustomUserDetails;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auctions")
@RequiredArgsConstructor
public class AuctionController {

    private final AuctionQueryService auctionQueryService;

    @GetMapping("/my/non-biddings")
    public List<LandmarkSimpleDto> findNonBiddingAuctions(
            @AuthenticationPrincipal CustomUserDetails member,
            @ModelAttribute AuctionSearchCondition condition
    ) {
        return AuctionMapper.toLandmarkSimpleDto(
                auctionQueryService.findNonBiddingAuctions(member.getId(), condition)
        );
    }

}
