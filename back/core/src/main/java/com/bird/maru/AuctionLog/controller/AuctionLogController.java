package com.bird.maru.AuctionLog.controller;

import com.bird.maru.AuctionLog.service.AuctionLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auction")
@Slf4j
public class AuctionLogController {
    private final AuctionLogService auctionLogService;


}
