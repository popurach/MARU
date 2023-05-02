package com.bird.maru.spot.service.query;

import com.bird.maru.spot.controller.dto.SpotSearchCondition;
import com.bird.maru.spot.repository.query.dto.SpotSimpleDto;
import java.util.List;

public interface SpotQueryService {

    List<SpotSimpleDto> findMySpots(Long memberId, SpotSearchCondition condition);

    List<SpotSimpleDto> findMyScraps(Long memberId, SpotSearchCondition condition);

    String findOwnerSpot(Long memberId, Long landmarkId);

}
