package com.bird.maru.cluster.geo;

import com.bird.maru.domain.model.type.Coordinate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Marker {

    private Long id;
    private Coordinate coordinate;

}
