package com.bird.maru.cluster.mapper;

import com.bird.maru.cluster.geo.Marker;
import com.bird.maru.cluster.geo.Property;
import com.bird.maru.domain.model.type.GeoType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.wololo.geojson.Feature;
import org.wololo.geojson.Point;

public class MarkerMapper {

    public static double[] toRawPoint(Marker marker) {
        return new double[] { marker.getCoordinate().getLng(), marker.getCoordinate().getLat() };
    }

    public static Feature toFeature(Marker marker) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> property = objectMapper.convertValue(Property.builder()
                                                                         .geoType(GeoType.POINT)
                                                                         .id(marker.getId())
                                                                         .build(),
                                                                 new TypeReference<Map<String, Object>>() {
                                                                 }
        );
        return new Feature(
                new Point(MarkerMapper.toRawPoint(marker)),
                property
        );

    }

}