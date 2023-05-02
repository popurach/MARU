package com.bird.maru.landmark.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.bird.maru.domain.model.entity.Landmark;
import com.bird.maru.landmark.controller.dto.LandmarkMapResponseDto;
import com.bird.maru.landmark.repository.query.LandmarkCustomQueryRepository;
import com.bird.maru.landmark.service.query.LandmarkQueryService;
import com.bird.maru.landmark.util.LandmarkUtil;
import com.bird.maru.member.repository.query.MemberRedisQueryRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@Slf4j
class LandmarkQueryServiceTest {

    @Autowired
    private LandmarkQueryService landmarkQueryService;
    @MockBean
    private LandmarkCustomQueryRepository landmarkCustomQueryRepository;
    @MockBean
    private MemberRedisQueryRepository memberRedisQueryRepository;
    private List<Landmark> landmarks;
    private final Set<Long> visitedLandmarks = new HashSet<>();

    @BeforeEach
    void setUp() {
        landmarks = LandmarkUtil.makeRandomSeoul();
        visitedLandmarks.add(1L);
        visitedLandmarks.add(2L);
        visitedLandmarks.add(5L);
        visitedLandmarks.add(8L);
        visitedLandmarks.add(11L);
    }

    @Test
    @DisplayName("랜드마크 지도 기반 조회 테스트")
    void findLandmarkBasedMapTest() {
        // given
        Double west = 126.231351;
        Double south = 36.215652;
        Double east = 126.34215;
        Double north = 36.351235;
        Long memberId = 1L;
        List<Landmark> landmarks1 = landmarks.subList(0, 20);
        given(landmarkCustomQueryRepository.findLandmarksBasedMap(west, south, east, north))
                .willReturn(landmarks1);
        given(memberRedisQueryRepository.findVisitedLandmarks(memberId))
                .willReturn(visitedLandmarks);
        // when
        List<LandmarkMapResponseDto> landmarkBasedMap = landmarkQueryService.findLandmarkBasedMap(west, south, east, north, memberId);

        int totalVisited = 0;
        for (LandmarkMapResponseDto l : landmarkBasedMap) {
            log.debug("{} {} {} {}", l.getId(), l.getCoordinate(), l.getName(), l.getVisited());
            if (l.getVisited()) {
                totalVisited++;
            }
        }
        // then
        assertThat(totalVisited).isEqualTo(visitedLandmarks.size());
        assertThat(landmarkBasedMap).hasSize(20);

    }

}
