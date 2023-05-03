package com.bird.maru.cluster;

import com.bird.maru.cluster.geo.BoundingBox;
import com.bird.maru.cluster.geo.Marker;
import com.bird.maru.cluster.geo.SuperClusterParams;
import com.bird.maru.cluster.util.DistanceMeasure;
import com.bird.maru.cluster.util.PointConverter;
import com.bird.maru.domain.model.type.Coordinate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.wololo.geojson.Feature;

@SpringBootTest
@Slf4j
class ClusteringTest {

    @Autowired
    private SuperClusterParams superClusterParams;
    @Autowired
    private PointConverter pointConverter;
    @Autowired
    private DistanceMeasure distanceMeasure;
    private List<Marker> markers;
    private BoundingBox boundingBox;
    private List<Marker> wantedMarkers;


    @BeforeEach
    void init() {
        wantedMarkers = new ArrayList<>();
        boundingBox = BoundingBox.builder()
                                 .west(126.9634795)
                                 .south(37.5462956)
                                 .east(126.9807529)
                                 .north(37.5587141)
                                 .zoom(15)
                                 .build();
        // 서울내에 랜덤 좌표 생성
        markers = makeRandomSeoul();
    }

    @Test
    @DisplayName("클러스터링 테스트")
    void clusteringTest() {
        // given
        log.debug("BoundingBox : {}\nSuperCluster Params : {} ",boundingBox, superClusterParams);
        SuperCluster cluster = SuperCluster.builder()
                                         .distanceMeasure(distanceMeasure)
                                         .superClusterParams(superClusterParams)
                                         .pointConverter(pointConverter)
                                         .points(wantedMarkers)
                                         .build();


        // when
        List<Feature> features = cluster.run(boundingBox);

        // then
        features.forEach(f -> log.debug("{}", f));
        log.debug("Before Clustering Markers size : {}\tAfter Clustering Markers size : {}",
                  wantedMarkers.size(), features.size());

    }


    private List<Marker> makeRandomSeoul() {
        final double SEOUL_LATITUDE = 37.5665;
        final double SEOUL_LONGITUDE = 126.9780;
        final double MAX_DISTANCE_IN_KM = 10.0; // 서울 내에서 생성할 좌표의 최대 거리 (10km)
        final int NUM_COORDINATES = 10000; // 생성할 좌표의 개수

        List<Marker> seoul = new ArrayList<>();

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
            Marker marker = Marker.builder()
                                  .id((long) i)
                                  .coordinate(Coordinate.builder()
                                                        .lng(longitude)
                                                        .lat(latitude)
                                                        .build())
                                  .build();
            if (boundingBox.getWest() <= longitude && longitude <= boundingBox.getEast()
                    && boundingBox.getSouth() <= latitude && latitude <= boundingBox.getNorth()) {
                wantedMarkers.add(marker);
            }
            seoul.add(marker);
        }
        return seoul;
    }


}
