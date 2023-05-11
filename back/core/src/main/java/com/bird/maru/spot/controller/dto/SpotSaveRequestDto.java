package com.bird.maru.spot.controller.dto;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
public class SpotSaveRequestDto {
    @Nullable
    @Size(max = 5)
    private final List<String> tags = new ArrayList<>();
    @Nullable
    private Long landmarkId;

}
