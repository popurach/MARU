package com.bird.maru.map.controller.dto;

import com.bird.maru.cluster.geo.BoundingBox;
import com.bird.maru.domain.model.type.MapFilterType;
import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
@ToString
public class MapCondition {

    @NotNull
    private BoundingBox boundingBox;
    @NotNull
    private MapFilterType filter;
    @Nullable
    private Long tagId;

}
