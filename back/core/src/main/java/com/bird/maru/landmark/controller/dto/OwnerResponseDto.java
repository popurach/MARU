package com.bird.maru.landmark.controller.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
public class OwnerResponseDto {

    private Long id;
    private String nickname;
    private String profileImageUrl;
    private String spotImageUrl;

}
