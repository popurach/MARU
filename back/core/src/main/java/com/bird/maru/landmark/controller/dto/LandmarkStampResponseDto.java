package com.bird.maru.landmark.controller.dto;

import java.net.URL;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
public class LandmarkStampResponseDto {

    private Long landmarkId;
    private String name;
    private Long spotId;
    private URL imageUrl;

}
