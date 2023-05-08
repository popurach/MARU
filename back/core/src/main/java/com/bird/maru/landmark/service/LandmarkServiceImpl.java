package com.bird.maru.landmark.service;

import com.bird.maru.common.exception.ResourceNotFoundException;
import com.bird.maru.domain.model.entity.Landmark;
import com.bird.maru.landmark.repository.query.LandmarkQueryRepository;
import com.bird.maru.member.repository.query.MemberRedisRepository;
import com.bird.maru.point.service.PointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LandmarkServiceImpl implements LandmarkService {

    private final MemberRedisRepository memberRedisRepository;
    private final PointService pointService;
    private final LandmarkQueryRepository landmarkQueryRepository;

    @Override
    public Integer visitLandmark(Long landmarkId, Long memberId) throws ResourceNotFoundException {
        if (Boolean.TRUE.equals(memberRedisRepository.existVisitedLandmark(memberId, landmarkId))) {
            return 0;
        }
        Landmark landmark = landmarkQueryRepository.findById(landmarkId)
                                                   .orElseThrow(() -> new ResourceNotFoundException("해당 리소스 존재하지 않습니다."));
        landmark.addCount();
        return pointService.landmarkVisiting(memberId);
    }

}
