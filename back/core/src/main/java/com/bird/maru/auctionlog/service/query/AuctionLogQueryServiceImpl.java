package com.bird.maru.auctionlog.service.query;

import com.bird.maru.auctionlog.controller.dto.AuctionLogSearchCondition;
import com.bird.maru.auctionlog.repository.query.AuctionLogCustomQueryRepository;
import com.bird.maru.domain.model.entity.AuctionLog;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuctionLogQueryServiceImpl implements AuctionLogQueryService {

    private final AuctionLogCustomQueryRepository auctionLogCustomQueryRepository;

    @Override
    public List<AuctionLog> findMyAuctions(Long memberId, AuctionLogSearchCondition condition) {
        return auctionLogCustomQueryRepository.findAllWithAuctionByMemberAndCondition(memberId, condition);
    }

}
