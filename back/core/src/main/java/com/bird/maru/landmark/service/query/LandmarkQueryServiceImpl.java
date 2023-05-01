package com.bird.maru.landmark.service.query;

import com.bird.maru.common.exception.ResourceNotFoundException;
import com.bird.maru.domain.model.entity.Landmark;
import com.bird.maru.landmark.repository.query.LandmarkQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LandmarkQueryServiceImpl implements LandmarkQueryService {

    private final LandmarkQueryRepository landmarkQueryRepository;

    /**
     * 랜드마크 조회
     *
     * @param id : landmark's id
     * @return Landmark Entity
     * @throws ResourceNotFoundException : DB에 해당하는 리소스 없음
     */
    @Override
    public Landmark findLandmark(Long id) throws ResourceNotFoundException {
        return landmarkQueryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("해당 리소스 존재하지 않습니다.")
        );
    }

}
