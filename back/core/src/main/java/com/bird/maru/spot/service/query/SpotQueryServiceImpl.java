package com.bird.maru.spot.service.query;

import com.bird.maru.common.util.TimeUtil;
import com.bird.maru.spot.controller.dto.SpotSearchCondition;
import com.bird.maru.spot.mapper.SpotMapper;
import com.bird.maru.spot.repository.query.SpotCustomQueryRepository;
import com.bird.maru.spot.repository.query.SpotQueryRepository;
import com.bird.maru.spot.repository.query.dto.SpotSimpleDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SpotQueryServiceImpl implements SpotQueryService {

    private final SpotQueryRepository spotQueryRepository;
    private final SpotCustomQueryRepository spotCustomQueryRepository;
    private final SpotMapper mapper;

    @Override
    public List<SpotSimpleDto> findMySpots(Long memberId, SpotSearchCondition condition) {
        List<Long> spotIds = spotCustomQueryRepository.findIdsByCondition(memberId, condition);
        List<SpotSimpleDto> dtos = mapper.toSpotSimpleDto(
                spotCustomQueryRepository.findAllWithTagsByIdIn(spotIds)
        );

        Set<Long> scrapedSpotIds = spotCustomQueryRepository.findScrapedIdsByIdIn(spotIds);

        return null;
    }

    @Override
    public String findOwnerSpot(Long memberId, Long landmarkId) {
        Random random = new Random();
        LocalDateTime previousAuctionStartDate = TimeUtil.getPreviousAuctionStartDate();
        List<String> ownerSpots = spotQueryRepository.findOwnerSpots(memberId, landmarkId, previousAuctionStartDate,
                                                                     TimeUtil.getPreviousAuctionEndDate(previousAuctionStartDate));

        return ownerSpots.isEmpty() ? null : ownerSpots.get(random.nextInt(ownerSpots.size()));
    }

}
