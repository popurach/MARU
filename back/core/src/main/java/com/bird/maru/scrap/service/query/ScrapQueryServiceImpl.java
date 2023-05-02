package com.bird.maru.scrap.service.query;

import com.bird.maru.scrap.repository.query.ScrapQueryRepository;
import com.bird.maru.spot.repository.query.dto.SpotSimpleDto;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ScrapQueryServiceImpl implements ScrapQueryService {

    private final ScrapQueryRepository scrapQueryRepository;

    @Override
    public void checkScraped(Long memberId, List<Long> spotIds, List<SpotSimpleDto> spotDtos) {
        Set<Long> scraped = scrapQueryRepository.findSpotIdsByMemberAndSpotIn(memberId, spotIds);
        spotDtos.forEach(dto -> {
            if (scraped.contains(dto.getId())) {
                dto.checkScraped();
            }
        });
    }

}
