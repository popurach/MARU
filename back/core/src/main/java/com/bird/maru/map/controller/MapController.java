package com.bird.maru.map.controller;

import com.bird.maru.auth.service.dto.CustomUserDetails;
import com.bird.maru.landmark.controller.dto.LandmarkMapResponseDto;
import com.bird.maru.landmark.service.query.LandmarkQueryService;
import com.bird.maru.map.controller.dto.MapCondition;
import com.bird.maru.map.service.query.MapQueryService;
import com.bird.maru.spot.service.query.SpotQueryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.wololo.geojson.Feature;

@RestController
@RequestMapping("/api/map")
@RequiredArgsConstructor
public class MapController {

    private final LandmarkQueryService landmarkQueryService;
    private final MapQueryService mapQueryService;

    /**
     * 랜드마크 지도 기준 검색 - 랜드마크는 클러스터링 진행하지 않습니다. <br/> 랜드마크 정보에는 해당 사용자의 최초 방문 여부를 함께 반환합니다. 결과가 존재하지 않다면 빈 리스트를 반환하고, <br/> 결과가 존재한다면 좌표 정보와 함께 사용자가
     * 방문했는지 정보를 같이 반환합니다.
     *
     * @param west   : minLng
     * @param south  : minLat
     * @param east   : maxLng
     * @param north  : maxLat
     * @param member : 현재 접근중인 주체
     * @return List<LandmarkMapResponseDto>
     */
    @GetMapping("/landmarks")
    public List<LandmarkMapResponseDto> findLandmarksBasedMap(
            @RequestParam Double west, @RequestParam Double south,
            @RequestParam Double east, @RequestParam Double north,
            @AuthenticationPrincipal CustomUserDetails member
    ) {
        return landmarkQueryService.findLandmarkBasedMap(west, south, east, north, member.getId());
    }

    /**
     * 지도 기반 스팟 목록 조회 - 클러스터 적용 <br/>
     * 단일 좌표 - properties.geoType = Point <br/>
     * 클러스터 대표 좌표 - properties.geoype = Cluster <br/>
     * 
     * @param member : 현재 접근중인 주체
     * @param condition : 검색 조건
     * @return 데이터 없을 시 빈 배열 반환
     * */
    @PostMapping("/spots")
    public List<Feature> spotsCluster(@AuthenticationPrincipal CustomUserDetails member, @RequestBody MapCondition condition) {
        return mapQueryService.spotsCluster(member.getId(), condition);
    }

}
