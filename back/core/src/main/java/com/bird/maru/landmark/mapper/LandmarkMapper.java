package com.bird.maru.landmark.mapper;

import com.bird.maru.domain.model.entity.Landmark;
import com.bird.maru.landmark.controller.dto.LandmarkResponseDto;

public class LandmarkMapper {

    public static LandmarkResponseDto toLandmarkResponseDto(Landmark landmark) {
        return LandmarkResponseDto.builder()
                                  .name(landmark.getName())
                                  .build();
    }

}
