package com.bird.maru.spot.service.query;

import com.bird.maru.common.util.TimeUtil;
import com.bird.maru.spot.repository.query.SpotQueryRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SpotQueryServiceImpl implements SpotQueryService {

    private final SpotQueryRepository spotQueryRepository;

    @Override
    public String findOwnerSpot(Long memberId, Long landmarkId) {
        Random random = new Random();
        LocalDateTime previousAuctionStartDate = TimeUtil.getPreviousAuctionStartDate();
        List<String> ownerSpots = spotQueryRepository.findOwnerSpots(memberId, landmarkId, previousAuctionStartDate,
                                                                     TimeUtil.getPreviousAuctionEndDate(previousAuctionStartDate));

        return ownerSpots.isEmpty() ? null : ownerSpots.get(random.nextInt(ownerSpots.size()));
    }

}
