package com.bird.maru.cluster.util;

import com.bird.maru.domain.model.type.CoordinateSystem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PointConverter {

    @Value("${cluster.crs}")
    private CoordinateSystem crs;

    public double convertLngToX(double lng) {
        if (crs.equals(CoordinateSystem.MERCATOR)) {
            return lng / 360 + 0.5;
        }
        return lng;
    }

    public double convertLatToY(double lat) {
        if (crs.equals(CoordinateSystem.MERCATOR)) {
            double sin = Math.sin(lat * Math.PI / 180);
            double y = (0.5 - 0.25 * Math.log((1 + sin) / (1 - sin)) / Math.PI);
            return y < 0 ? 0 : y > 1 ? 1 : y;
        }
        return lat;
    }

    public double convertXToLng(double x) {
        if (crs.equals(CoordinateSystem.MERCATOR)) {
            return (x - 0.5) * 360;
        }
        return x;
    }

    public double convertYToLat(double y) {
        if (crs.equals(CoordinateSystem.MERCATOR)) {
            double y2 = (180 - y * 360) * Math.PI / 180;
            return 360 * Math.atan(Math.exp(y2)) / Math.PI - 90;
        }
        return y;
    }

}
