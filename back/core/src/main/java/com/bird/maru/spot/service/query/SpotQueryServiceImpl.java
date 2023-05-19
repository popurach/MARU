package com.bird.maru.spot.service.query;

import com.bird.maru.common.exception.ResourceNotFoundException;
import com.bird.maru.common.util.RandomUtil;
import com.bird.maru.common.util.TimeUtil;
import com.bird.maru.domain.model.entity.Spot;
import com.bird.maru.domain.model.entity.Tag;
import com.bird.maru.like.service.query.LikeQueryService;
import com.bird.maru.scrap.repository.query.ScrapQueryRepository;
import com.bird.maru.scrap.service.query.ScrapQueryService;
import com.bird.maru.spot.controller.dto.SpotDetailResponseDto;
import com.bird.maru.spot.controller.dto.SpotMapCondition;
import com.bird.maru.spot.controller.dto.SpotSearchCondition;
import com.bird.maru.spot.mapper.SpotMapper;
import com.bird.maru.spot.repository.query.SpotCustomQueryRepository;
import com.bird.maru.spot.repository.query.SpotQueryRepository;
import com.bird.maru.spot.repository.query.dto.SpotSimpleDto;
import com.bird.maru.tag.repository.query.TagCustomQueryRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class SpotQueryServiceImpl implements SpotQueryService {

    private final SpotQueryRepository spotQueryRepository;
    private final SpotCustomQueryRepository spotCustomQueryRepository;
    private final LikeQueryService likeQueryService;
    private final ScrapQueryService scrapQueryService;
    private final TagCustomQueryRepository tagCustomQueryRepository;
    private final ScrapQueryRepository scrapQueryRepository;

    /**
     * 먼저 조건에 맞는 Spot ID의 목록을 조회합니다. 이후 조회한 Spot ID의 목록으로 Tag를 포함한 Spot 목록을 조회합니다. 그 결과를 DTO 변환한 후에 좋아요 여부와 스크랩 여부를 확인합니다. 이 비즈니스 로직을 수행하는 과정에서 총 4번의
     * 쿼리가 수행됩니다.
     *
     * @param memberId  현재 로그인한 회원의 ID
     * @param condition Spot 조회 조건
     * @return 현재 페이지에 맞는 Spot 목록
     */
    @Override
    public List<SpotSimpleDto> findMySpots(Long memberId, SpotSearchCondition condition) {
        List<Long> spotIds = spotCustomQueryRepository.findIdsByMemberAndMineCondition(memberId, condition);
        List<SpotSimpleDto> spotDtos = SpotMapper.toSpotSimpleDto(
                spotCustomQueryRepository.findAllWithTagsByIdIn(spotIds)
        );

        likeQueryService.checkLiked(memberId, spotIds, spotDtos);
        scrapQueryService.checkScraped(memberId, spotIds, spotDtos);
        return spotDtos;
    }

    @Override
    public List<SpotSimpleDto> findMyScraps(Long memberId, SpotSearchCondition condition) {
        List<Spot> spots = spotCustomQueryRepository.findAllByMemberAndScrapCondition(memberId, condition);
        List<Long> spotIds = spots.stream()
                                  .map(Spot::getId)
                                  .collect(Collectors.toList());

        Map<Long, List<Tag>> tagMap = tagCustomQueryRepository.findAllWithTagBySpotIn(spotIds);
        List<SpotSimpleDto> spotDtos = SpotMapper.toSpotSimpleDto(spots, tagMap);

        likeQueryService.checkLiked(memberId, spotIds, spotDtos);
        return spotDtos;
    }

    @Override
    public String findOwnerSpot(Long memberId, Long landmarkId) {
        LocalDateTime previousAuctionStartDate = TimeUtil.getPreviousAuctionStartDate();
        List<String> ownerSpots = spotQueryRepository.findOwnerSpots(
                memberId, landmarkId,
                previousAuctionStartDate, TimeUtil.getPreviousAuctionEndDate(previousAuctionStartDate)
        );

        return ownerSpots.isEmpty() ? null : RandomUtil.randomElement(ownerSpots);
    }

    /**
     * 랜드마크에 포함된 모든 스팟 조회
     *
     * @param landmarkId : 랜드마크 id
     * @param size       : 페이지네이션 size
     * @param lastOffset : 이전 페이지의 마지막 아이템의 id
     * @return Spot 리스트
     */
    @Override
    public List<Spot> findLandmarkSpots(Long landmarkId, Long lastOffset, Integer size) {
        return spotCustomQueryRepository.findSpotByLandmark(landmarkId, lastOffset, size);
    }

    /**
     * 스팟 상세 보기
     *
     * @param spotId   : 스팟 id
     * @param memberId : 사용자 id
     * @return SpotDetailResponseDto
     * @throws ResourceNotFoundException : 리소스 없음
     */
    @Override
    public SpotDetailResponseDto findSpotDetail(Long spotId, Long memberId) throws ResourceNotFoundException {
        SpotDetailResponseDto spotDetail = spotCustomQueryRepository.findSpotDetail(spotId, memberId).orElseThrow(
                () -> new ResourceNotFoundException("해당 리소스 존재하지 않습니다."));
        List<Tag> tags = tagCustomQueryRepository.findAllBySpotId(spotId);
        spotDetail.setTags(tags);
        return spotDetail;
    }

    /**
     * 현재 지도 내 스팟 목록 조회 API
     *
     * @param condition : 지도 영역, 전체 스팟 | 내 스팟, 마지막 item index, 페이지 사이즈
     * @param memberId  : 현재 사용자 id
     * @return List<SpotSimpleDto> 스팟 목록
     */
    @Override
    public List<SpotSimpleDto> findSpotsBasedMap(SpotMapCondition condition, Long memberId) {
        List<Long> spotIds = spotCustomQueryRepository.findIdsBasedMap(condition, memberId);
        log.debug("{}", spotIds);
        if (spotIds.isEmpty()) {
            return new ArrayList<>();
        }
        List<SpotSimpleDto> spotBasedMap = spotCustomQueryRepository.findSpotBasedMap(spotIds);
        log.debug("{}", spotBasedMap);
        Set<Long> scrapSpotIds = scrapQueryRepository.findSpotIdsByMemberAndSpotIn(memberId, spotIds);
        log.debug("{}", scrapSpotIds);
        spotBasedMap.forEach(s -> {
            if (scrapSpotIds.contains(s.getId())) {
                s.checkScraped();
            }
        });
        return spotBasedMap;
    }

}
