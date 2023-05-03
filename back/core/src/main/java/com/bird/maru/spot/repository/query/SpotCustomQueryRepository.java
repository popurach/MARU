package com.bird.maru.spot.repository.query;

import static com.bird.maru.domain.model.entity.QScrap.scrap;
import static com.bird.maru.domain.model.entity.QSpot.spot;
import static com.bird.maru.domain.model.entity.QSpotHasTag.spotHasTag;
import static com.bird.maru.domain.model.entity.QTag.tag;

import com.bird.maru.domain.model.entity.Spot;
import com.bird.maru.spot.controller.dto.SpotSearchCondition;
import com.bird.maru.spot.repository.query.dto.SpotSimpleDto;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SpotCustomQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<Long> findIdsByMemberAndMineCondition(Long memberId, SpotSearchCondition condition) {
        return queryFactory.select(spot.id)
                           .from(spot)
                           .where(
                                   spot.deleted.isFalse(),
                                   spot.member.id.eq(memberId),
                                   ltSpotOffset(condition.getLastOffset())
                           )
                           .orderBy(offsetOrder(condition))
                           .limit(condition.getSize())
                           .fetch();
    }

    public List<Spot> findAllWithTagsByIdIn(List<Long> spotIds) {
        return queryFactory.selectFrom(spot)
                           .leftJoin(spot.tags, spotHasTag).fetchJoin()
                           .leftJoin(spotHasTag.tag, tag).fetchJoin()
                           .where(spot.id.in(spotIds))
                           .orderBy(spot.id.desc())
                           .fetch();
    }

    public List<Spot> findAllByMemberAndScrapCondition(Long memberId, SpotSearchCondition condition) {
        return queryFactory.select(scrap.spot)
                           .from(scrap)
                           .join(scrap.spot, spot)
                           .where(
                                   scrap.deleted.isFalse(),
                                   spot.deleted.isFalse(),
                                   scrap.member.id.eq(memberId),
                                   ltScrapOffset(memberId, condition.getLastOffset())
                           )
                           .orderBy(offsetOrder(condition))
                           .limit(condition.getSize())
                           .fetch();
    }

    public List<SpotSimpleDto> findSpotByMyVisitedLandmark(Long memberId, List<Long> landmarkIds) {
        return queryFactory.select(Projections.constructor(SpotSimpleDto.class,
                                                           spot.id.as("id"),
                                                           spot.landmark.id.as("landmarkId"),
                                                           Expressions.asString(spot.image.url.toString()).as("imageUrl")
                           ))
                           .from(spot)
                           .where(spot.id.in(
                                   JPAExpressions.select(spot.id.max())
                                                 .from(spot)
                                                 .where(spot.landmark.id.in(landmarkIds),
                                                        spot.deleted.isFalse(),
                                                        spot.member.id.eq(memberId))
                                                 .groupBy(spot.landmark.id)
                           ))
                           .fetch();
    }

    public List<Spot> findSpotByLandmark(Long landmarkId, Long lastOffset, Integer size) {
        return queryFactory.selectFrom(spot)
                .where(spot.landmark.id.eq(landmarkId),
                       spot.deleted.isFalse(),
                       ltSpotOffset(lastOffset))
                .orderBy(spot.id.desc())
                .limit(size)
                .fetch();
    }

    private BooleanExpression ltSpotOffset(Long lastOffset) {
        return lastOffset == null ? null : spot.id.lt(lastOffset);
    }

    private BooleanExpression ltScrapOffset(Long memberId, Long lastOffset) {
        if (lastOffset == null) {
            return null;
        }

        return scrap.modifiedDateTime.before(queryModifiedDateTimeByMemberAndSpot(memberId, lastOffset))
                                     .or(
                                             scrap.modifiedDateTime.eq(queryModifiedDateTimeByMemberAndSpot(memberId, lastOffset))
                                                                   .and(scrap.spot.id.lt(lastOffset))
                                     );
    }

    private JPQLQuery<LocalDateTime> queryModifiedDateTimeByMemberAndSpot(Long memberId, Long lastOffset) {
        return JPAExpressions.select(scrap.modifiedDateTime)
                             .from(scrap)
                             .where(scrap.member.id.eq(memberId),
                                    scrap.spot.id.eq(lastOffset));
    }

    private OrderSpecifier<?>[] offsetOrder(SpotSearchCondition condition) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        if (Boolean.TRUE.equals(condition.getScraped())) {
            orderSpecifiers.add(scrap.modifiedDateTime.desc());
        }

        orderSpecifiers.add(spot.id.desc());
        return orderSpecifiers.toArray(new OrderSpecifier<?>[0]);
    }

}
