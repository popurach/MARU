package com.bird.maru.landmark.mapper;

import com.bird.maru.domain.model.entity.Landmark;
import com.bird.maru.landmark.controller.dto.LandmarkMapResponseDto;
import com.bird.maru.landmark.controller.dto.LandmarkResponseDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LandmarkMapper {

    public static LandmarkResponseDto toLandmarkResponseDto(Landmark landmark) {
        return LandmarkResponseDto.builder()
                                  .name(landmark.getName())
                                  .build();
    }

    public static List<LandmarkMapResponseDto> toLandmarkMapResponseDto(List<Landmark> landmarks, Map<Long, Boolean> visited) {
        List<LandmarkMapResponseDto> responseDtos = new ArrayList<>();
        landmarks.forEach(
                l -> responseDtos.add(
                        LandmarkMapResponseDto.builder()
                                              .id(l.getId())
                                              .coordinate(l.getCoordinate())
                                              .name(l.getName())
                                              .visited(visited.containsKey(l.getId()))
                                              .build()));
        return responseDtos;
    }

}
