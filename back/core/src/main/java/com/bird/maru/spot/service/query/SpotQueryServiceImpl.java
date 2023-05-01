package com.bird.maru.spot.service.query;

import com.bird.maru.common.util.TimeUtil;
import com.bird.maru.like.repository.query.LikeQueryRepository;
import com.bird.maru.scrap.repository.query.ScrapQueryRepository;
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
    private final LikeQueryRepository likeQueryRepository;
    private final ScrapQueryRepository scrapQueryRepository;
    private final SpotMapper mapper;

    /**
     * 먼저 조건에 맞는 Spot ID의 목록을 조회합니다. 이후 조회한 Spot ID의 목록으로 Tag를 포함한 Spot 목록을 조회합니다.
     * 그 결과를 DTO 변환한 후에 좋아요 여부와 스크랩 여부를 확인합니다. 이 비즈니스 로직을 수행하는 과정에서 총 4번의 쿼리가 수행됩니다.
     *
     * @param memberId  현재 로그인한 회원의 ID
     * @param condition Spot 조회 조건
     * @return 현재 페이지에 맞는 Spot 목록
     */
    @Override
    public List<SpotSimpleDto> findMySpots(Long memberId, SpotSearchCondition condition) {
        List<Long> spotIds = spotCustomQueryRepository.findIdsByCondition(memberId, condition);
        List<SpotSimpleDto> spotDtos = mapper.toSpotSimpleDto(
                spotCustomQueryRepository.findAllWithTagsByIdIn(spotIds)
        );

        checkLiked(memberId, spotIds, spotDtos);
        checkScraped(memberId, spotIds, spotDtos);
        return spotDtos;
    }

    @Override
    public String findOwnerSpot(Long memberId, Long landmarkId) {
        Random random = new Random();
        LocalDateTime previousAuctionStartDate = TimeUtil.getPreviousAuctionStartDate();
        List<String> ownerSpots = spotQueryRepository.findOwnerSpots(memberId, landmarkId, previousAuctionStartDate,
                                                                     TimeUtil.getPreviousAuctionEndDate(previousAuctionStartDate));

        return ownerSpots.isEmpty() ? null : ownerSpots.get(random.nextInt(ownerSpots.size()));
    }

    private void checkLiked(Long memberId, List<Long> spotIds, List<SpotSimpleDto> spotDtos) {
        Set<Long> liked = likeQueryRepository.findSpotIdsByMemberAndSpotIn(memberId, spotIds);
        spotDtos.forEach(dto -> {
            if (liked.contains(dto.getId())) {
                dto.checkLiked();
            }
        });

    }

    private void checkScraped(Long memberId, List<Long> spotIds, List<SpotSimpleDto> spotDtos) {
        Set<Long> scraped = scrapQueryRepository.findSpotIdsByMemberAndSpotIn(memberId, spotIds);
        spotDtos.forEach(dto -> {
            if (scraped.contains(dto.getId())) {
                dto.checkScraped();
            }
        });
    }

}
