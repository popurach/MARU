package com.bird.maru.spot.controller.dto;

import com.bird.maru.domain.model.type.MapFilterType;
import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
@ToString
public class SpotMapCondition {

    @NotNull
    private Double west;
    @NotNull
    private Double south;
    @NotNull
    private Double east;
    @NotNull
    private Double north;
    @NotNull
    private MapFilterType filter;
    @Nullable
    private Long lastOffset;
    @Builder.Default
    private Integer size = 20;
    @Nullable
    private Long tagId;

}
