package com.bird.maru.spot.service.query;

import com.bird.maru.common.exception.ResourceNotFoundException;
import com.bird.maru.domain.model.entity.Spot;
import com.bird.maru.spot.controller.dto.SpotDetailResponseDto;
import com.bird.maru.spot.controller.dto.SpotMapCondition;
import com.bird.maru.spot.controller.dto.SpotSearchCondition;
import com.bird.maru.spot.repository.query.dto.SpotSimpleDto;
import java.util.List;

public interface SpotQueryService {

    List<SpotSimpleDto> findMySpots(Long memberId, SpotSearchCondition condition);

    List<SpotSimpleDto> findMyScraps(Long memberId, SpotSearchCondition condition);

    String findOwnerSpot(Long memberId, Long landmarkId);

    List<Spot> findLandmarkSpots(Long landmarkId, Long lastOffset, Integer size);

    SpotDetailResponseDto findSpotDetail(Long spotId, Long memberId) throws ResourceNotFoundException;

    List<SpotSimpleDto> findSpotsBasedMap(SpotMapCondition condition, Long memberId);

}
