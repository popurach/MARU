package com.bird.maru.landmark.repository.query;

import static com.bird.maru.domain.model.entity.QLandmark.landmark;

import com.bird.maru.domain.model.entity.Landmark;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class LandmarkCustomQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<Landmark> findAll(Long lastOffset, Integer size) {
        return jpaQueryFactory.selectFrom(landmark)
                .where(gtOffset(lastOffset))
                .orderBy(landmark.id.asc())
                .limit(size)
                .fetch();
    }

    public List<Landmark> findLandmarksBasedMap(Double west, Double south, Double east, Double north) {
        return jpaQueryFactory.selectFrom(landmark)
                              .where(landmark.coordinate.lng.between(west, east),
                                     landmark.coordinate.lat.between(south, north))
                              .fetch();
    }

    public BooleanExpression gtOffset(Long offset) {
        return offset == null ? null : landmark.id.gt(offset);
    }

}
