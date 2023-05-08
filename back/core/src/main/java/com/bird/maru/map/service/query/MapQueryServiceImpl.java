package com.bird.maru.map.service.query;

import com.bird.maru.cluster.SuperCluster;
import com.bird.maru.cluster.geo.BoundingBox;
import com.bird.maru.cluster.geo.Marker;
import com.bird.maru.cluster.geo.SuperClusterParams;
import com.bird.maru.cluster.util.DistanceMeasure;
import com.bird.maru.cluster.util.PointConverter;
import com.bird.maru.map.controller.dto.MapCondition;
import com.bird.maru.spot.repository.query.SpotCustomQueryRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wololo.geojson.Feature;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MapQueryServiceImpl implements MapQueryService {

    private final SpotCustomQueryRepository spotCustomQueryRepository;
    private final SuperClusterParams superClusterParams;
    private final PointConverter pointConverter;
    private final DistanceMeasure distanceMeasure;


    /**
     * 지도 내 스팟 클러스터링 진행 <br/> 1. 지도 내 모든 클러스터링 조회 <br/> - Redis 조회 <br/> - RDBMS 조회 <br/> 2. 필터링 <br/> 3. 클러스터링 <br/>
     * 4. RDBMS에서 조회한 좌표 Redis에 캐싱 <br/>
     *
     * @param memberId  : 현재 접근중인 주체
     * @param condition : 조회 조건
     */
    @Override
    public List<Feature> spotsCluster(Long memberId, MapCondition condition) {
        BoundingBox boundingBox = condition.getBoundingBox();
        // Redis 조회 필요
        List<Marker> markers = spotCustomQueryRepository.findMarkerByBoundingBox(boundingBox);
        if (Boolean.TRUE.equals(condition.getMine())) {
            markers = markers.stream().filter(marker -> marker.getMemberId().equals(memberId)).collect(Collectors.toList());
        }
        if (markers.isEmpty()) {
            return new ArrayList<>();
        }
        // Event 발생시켜 Redis에 RDBMS에서 조회한 좌표 저장시키기
        SuperCluster superCluster = new SuperCluster(superClusterParams, markers, pointConverter, distanceMeasure);
        return superCluster.run(boundingBox);
    }

}
