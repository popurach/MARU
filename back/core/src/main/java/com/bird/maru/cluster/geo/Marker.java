package com.bird.maru.cluster.geo;

import com.bird.maru.domain.model.type.Coordinate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Marker {

    private Long id;
    private Coordinate coordinate;

}
