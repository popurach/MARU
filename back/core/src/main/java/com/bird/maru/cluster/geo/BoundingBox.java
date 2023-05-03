package com.bird.maru.cluster.geo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@SuperBuilder
@ToString
public class BoundingBox {

    private Double west;
    private Double south;
    private Double east;
    private Double north;
    private Integer zoom;

    public double[] getBoundingBox() {
        return new double[] { west, south, east, north };
    }

}
