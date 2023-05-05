package com.bird.maru.scrap.service.query;

import com.bird.maru.spot.repository.query.dto.SpotSimpleDto;
import java.util.List;

public interface ScrapQueryService {

    void checkScraped(Long memberId, List<Long> spotIds, List<SpotSimpleDto> spotDtos);

}
