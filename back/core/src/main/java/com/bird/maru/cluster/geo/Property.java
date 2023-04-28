package com.bird.maru.cluster.geo;

import com.bird.maru.domain.model.type.GeoType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Property {

    private GeoType geoType;
    private Long id;
    private Double radius;
    private Integer count;
    private String abbrevCount;

}
