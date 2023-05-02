package com.bird.maru.landmark.mapper;

import com.bird.maru.domain.model.entity.Landmark;
import com.bird.maru.landmark.controller.dto.LandmarkMapResponseDto;
import com.bird.maru.landmark.controller.dto.LandmarkResponseDto;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class LandmarkMapper {

    public static LandmarkResponseDto toLandmarkResponseDto(Landmark landmark) {
        return LandmarkResponseDto.builder()
                                  .name(landmark.getName())
                                  .build();
    }

    public static LandmarkMapResponseDto toLandmarkMapResponseDto(Landmark landmark, Set<Long> visited) {
        return LandmarkMapResponseDto.builder()
                                     .id(landmark.getId())
                                     .coordinate(landmark.getCoordinate())
                                     .name(landmark.getName())
                                     .visited(visited.contains(landmark.getId()))
                                     .build();
    }

    public static List<LandmarkMapResponseDto> toLandmarkMapResponseDtos(List<Landmark> landmarks, Set<Long> visited) {
        return landmarks.stream().map(
                l -> toLandmarkMapResponseDto(l, visited)
        ).collect(Collectors.toList());
    }

}
