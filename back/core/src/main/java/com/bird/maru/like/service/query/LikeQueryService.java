package com.bird.maru.like.service.query;

import com.bird.maru.spot.repository.query.dto.SpotSimpleDto;
import java.util.List;

public interface LikeQueryService {

    void checkLiked(Long memberId, List<Long> spotIds, List<SpotSimpleDto> spotDtos);

}
