package com.bird.maru.auction.service.query;

import com.bird.maru.auction.controller.dto.AuctionDetailDto;
import com.bird.maru.auction.controller.dto.AuctionSearchCondition;
import com.bird.maru.auction.mapper.AuctionMapper;
import com.bird.maru.auction.repository.query.AuctionCustomQueryRepository;
import com.bird.maru.auctionlog.repository.query.AuctionLogQueryRepository;
import com.bird.maru.common.exception.ResourceNotFoundException;
import com.bird.maru.domain.model.entity.Auction;
import com.bird.maru.domain.model.entity.Landmark;
import com.bird.maru.landmark.repository.query.LandmarkQueryRepository;
import com.bird.maru.spot.repository.query.SpotCustomQueryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuctionQueryServiceImpl implements AuctionQueryService {

    private final LandmarkQueryRepository landmarkQueryRepository;
    private final AuctionLogQueryRepository auctionLogQueryRepository;
    private final SpotCustomQueryRepository spotCustomQueryRepository;
    private final AuctionCustomQueryRepository auctionCustomQueryRepository;

    @Override
    public List<Auction> findMyNonBiddingAuctions(Long memberId, AuctionSearchCondition condition) {
        List<Long> visitedLandmarkIds = spotCustomQueryRepository.findLandmarkIdsByMemberAndCondition(memberId, condition);
        return auctionCustomQueryRepository.findAllByMemberAndLandmarkIdIn(memberId, visitedLandmarkIds, condition.getSize());
    }

    @Override
    public AuctionDetailDto findAuctionDetail(Long memberId, Long landmarkId) {
        Landmark landmark = landmarkQueryRepository.findById(landmarkId)
                                                   .orElseThrow(() -> new ResourceNotFoundException("해당 리소스를 찾을 수 없습니다."));

        return auctionLogQueryRepository.findByMemberAndLandmark(memberId, landmarkId)
                                        .map(auctionLog -> AuctionMapper.toAuctionDetailDto(landmark, auctionLog))
                                        .orElseGet(() -> AuctionMapper.toAuctionDetailDto(landmark));
    }

}
