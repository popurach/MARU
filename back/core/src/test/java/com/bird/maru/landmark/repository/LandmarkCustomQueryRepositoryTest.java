package com.bird.maru.landmark.repository;


import static org.assertj.core.api.Assertions.assertThat;

import com.bird.maru.cluster.geo.BoundingBox;
import com.bird.maru.domain.model.entity.Landmark;
import com.bird.maru.landmark.repository.query.LandmarkCustomQueryRepository;
import com.bird.maru.landmark.util.LandmarkUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Slf4j
class LandmarkCustomQueryRepositoryTest {

    @Autowired
    private LandmarkCustomQueryRepository ladLandmarkCustomQueryRepository;
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private LandmarkRepository landmarkRepository;

    private List<Landmark> landmarks = new ArrayList<>();

    private BoundingBox boundingBox;

    @BeforeEach
    void setUp() {
        landmarks = LandmarkUtil.makeRandomSeoul();
        boundingBox = BoundingBox.builder()
                                 .south(37.5462956)
                                 .west(126.9634795)
                                 .east(126.9807529)
                                 .north(37.5587141)
                                 .zoomLevel(15)
                                 .build();
    }

    @Test
    @DisplayName("랜드마크 페이지네이션 테스트")
    @Transactional
    void landmarkPaginationTest() {
        // given
        // when
        landmarks.forEach(l -> entityManager.persist(l));
        entityManager.flush();

        List<Landmark> landmarksBasedMap = ladLandmarkCustomQueryRepository.findLandmarksBasedMap(
                boundingBox.getWest(), boundingBox.getSouth(), boundingBox.getEast(), boundingBox.getNorth());
        log.debug("{}", landmarksBasedMap);

        // then
        assertThat(landmarksBasedMap).hasSizeLessThan(10000);
    }

}
