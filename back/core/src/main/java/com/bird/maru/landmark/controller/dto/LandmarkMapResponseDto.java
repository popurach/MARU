package com.bird.maru.landmark.controller.dto;

import com.bird.maru.domain.model.type.Coordinate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
public class LandmarkMapResponseDto {

    private Long id;
    private String name;
    private Coordinate coordinate;
    private Boolean visited;

}
