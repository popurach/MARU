package com.bird.maru.like.service.query;

import com.bird.maru.like.repository.query.LikeQueryRepository;
import com.bird.maru.spot.repository.query.dto.SpotSimpleDto;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LikeQueryServiceImpl implements LikeQueryService {

    private final LikeQueryRepository likeQueryRepository;

    @Override
    public void checkLiked(Long memberId, List<Long> spotIds, List<SpotSimpleDto> spotDtos) {
        Set<Long> liked = likeQueryRepository.findSpotIdsByMemberAndSpotIn(memberId, spotIds);
        spotDtos.forEach(dto -> {
            if (liked.contains(dto.getId())) {
                dto.checkLiked();
            }
        });
    }

}
