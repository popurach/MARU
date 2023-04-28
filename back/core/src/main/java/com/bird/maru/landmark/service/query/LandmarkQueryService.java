package com.bird.maru.landmark.service.query;

import com.bird.maru.common.exception.ResourceNotFoundException;
import com.bird.maru.domain.model.entity.Landmark;

public interface LandmarkQueryService {

    Landmark findLandmark(Long id) throws ResourceNotFoundException;

}
