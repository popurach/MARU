package com.bird.maru.auction.service.query;

import com.bird.maru.auction.controller.dto.AuctionSearchCondition;
import com.bird.maru.auction.repository.query.AuctionCustomQueryRepository;
import com.bird.maru.domain.model.entity.Auction;
import com.bird.maru.spot.repository.query.SpotCustomQueryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuctionQueryServiceImpl implements AuctionQueryService {

    private final SpotCustomQueryRepository spotCustomQueryRepository;
    private final AuctionCustomQueryRepository auctionCustomQueryRepository;

    @Override
    public List<Auction> findMyNonBiddingAuctions(Long memberId, AuctionSearchCondition condition) {
        List<Long> visitedLandmarkIds = spotCustomQueryRepository.findLandmarkIdsByMemberAndCondition(memberId, condition);
        return auctionCustomQueryRepository.findAllByMemberAndLandmarkIdIn(memberId, visitedLandmarkIds, condition.getSize());
    }

}
