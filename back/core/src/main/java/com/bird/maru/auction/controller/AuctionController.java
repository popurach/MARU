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

    /**
     * 내가 참여할 수 있는 경매 목록을 조회합니다. 현재는 경매에 참여하지 않은 것들입니다.
     *
     * @param member    현재 로그인 한 회원
     * @param condition 마지막 랜드마크 ID와 Page Size
     * @return 참여할 수 있는 경매의 랜드마크 목록
     */
    @GetMapping("/my/non-biddings")
    public List<LandmarkSimpleDto> findMyNonBiddingAuctions(
            @AuthenticationPrincipal CustomUserDetails member,
            @ModelAttribute AuctionSearchCondition condition
    ) {
        return AuctionMapper.toLandmarkSimpleDto(
                auctionQueryService.findMyNonBiddingAuctions(member.getId(), condition)
        );
    }

}
