package com.bird.maru.auction.controller;

import com.bird.maru.auction.service.query.AuctionQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auctions")
@RequiredArgsConstructor
public class AuctionController {

    private final AuctionQueryService auctionQueryService;

}
