package com.bird.maru.landmark.util;

import com.bird.maru.domain.model.entity.Landmark;
import com.bird.maru.domain.model.type.Coordinate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LandmarkUtil {

    public static List<Landmark> makeRandomSeoul() {
        final double SEOUL_LATITUDE = 37.5665;
        final double SEOUL_LONGITUDE = 126.9780;
        final double MAX_DISTANCE_IN_KM = 10.0; // 서울 내에서 생성할 좌표의 최대 거리 (10km)
        final int NUM_COORDINATES = 10000; // 생성할 좌표의 개수

        List<Landmark> seoul = new ArrayList<>();

        Random random = new Random();
        for (int i = 0; i < NUM_COORDINATES; i++) {
            double distance = random.nextDouble() * MAX_DISTANCE_IN_KM;
            double bearing = random.nextDouble() * 360.0;

            double lat1 = Math.toRadians(SEOUL_LATITUDE);
            double lon1 = Math.toRadians(SEOUL_LONGITUDE);
            double angularDistance = distance / 6371.0;

            double lat2 = Math.asin(Math.sin(lat1) * Math.cos(angularDistance) +
                                            Math.cos(lat1) * Math.sin(angularDistance) * Math.cos(Math.toRadians(bearing)));
            double lon2 = lon1 + Math.atan2(Math.sin(Math.toRadians(bearing)) *
                                                    Math.sin(angularDistance) * Math.cos(lat1),
                                            Math.cos(angularDistance) - Math.sin(lat1) * Math.sin(lat2));

            double latitude = Math.toDegrees(lat2);
            double longitude = Math.toDegrees(lon2);
            Landmark marker = Landmark.builder()
                                      .visitCount(0)
                                      .name("랜드마크다!!" + i)
                                      .coordinate(Coordinate.builder()
                                                            .lng(longitude)
                                                            .lat(latitude)
                                                            .build())
                                      .build();
            seoul.add(marker);
        }
        return seoul;
    }

}
