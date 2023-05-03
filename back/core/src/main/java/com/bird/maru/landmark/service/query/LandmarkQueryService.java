package com.bird.maru.landmark.service.query;

import com.bird.maru.common.exception.ResourceNotFoundException;
import com.bird.maru.domain.model.entity.Landmark;
import com.bird.maru.landmark.controller.dto.LandmarkMapResponseDto;
import java.util.List;

public interface LandmarkQueryService {

    Landmark findLandmark(Long id) throws ResourceNotFoundException;

    List<LandmarkMapResponseDto> findLandmarkBasedMap(Double west, Double south, Double east, Double north, Long memberId);

}
