package com.bird.maru.auctionlog.service.query;

import com.bird.maru.auctionlog.controller.dto.AuctionLogSearchCondition;
import com.bird.maru.domain.model.entity.AuctionLog;
import java.util.List;

public interface AuctionLogQueryService {

    List<AuctionLog> findMyAuctions(Long memberId, AuctionLogSearchCondition condition);

}
