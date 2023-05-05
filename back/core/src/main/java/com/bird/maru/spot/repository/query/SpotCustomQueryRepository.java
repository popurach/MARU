package com.bird.maru.spot.repository.query;

import static com.bird.maru.domain.model.entity.QScrap.scrap;
import static com.bird.maru.domain.model.entity.QSpot.spot;
import static com.bird.maru.domain.model.entity.QSpotHasTag.spotHasTag;
import static com.bird.maru.domain.model.entity.QTag.tag;

import com.bird.maru.cluster.geo.BoundingBox;
import com.bird.maru.cluster.geo.Marker;
import com.bird.maru.auction.controller.dto.AuctionSearchCondition;
import com.bird.maru.common.util.TimeUtil;
import com.bird.maru.domain.model.entity.Spot;
import com.bird.maru.spot.controller.dto.SpotSearchCondition;
import com.bird.maru.spot.repository.query.dto.SpotSimpleDto;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    public List<Long> findLandmarkIdsByMemberAndCondition(Long memberId, AuctionSearchCondition condition) {
        return queryFactory.select(spot.landmark.id)
                           .from(spot)
                           .where(
                                   spot.member.id.eq(memberId),
                                   spot.landmark.isNotNull(),
                                   spot.createdDateTime.after(TimeUtil.getThisWeekStartDateTime()),
                                   gtLandmarkOffset(condition.getLastOffset())
                           )
                           .orderBy(spot.landmark.id.asc())
                           .limit(condition.getSize() + 10L)
                           .fetch();
    }

    public List<SpotSimpleDto> findSpotByMyVisitedLandmark(Long memberId, List<Long> landmarkIds) {
        return queryFactory.select(Projections.constructor(SpotSimpleDto.class,
                                                           spot.id.as("id"),
                                                           spot.landmark.id.as("landmarkId"),
                                                           spot.image.url.as("imageUrl")
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

    public List<Marker> findMarkerByBoundingBox(BoundingBox boundingBox) {
        return queryFactory.select(Projections.fields(Marker.class,
                                                      spot.id.as("id"),
                                                      spot.member.id.as("memberId"),
                                                      spot.coordinate.as("coordinate")
                           ))
                           .from(spot)
                           .where(spot.coordinate.lng.between(boundingBox.getWest(), boundingBox.getEast()),
                                  spot.coordinate.lat.between(boundingBox.getSouth(), boundingBox.getNorth()),
                                  spot.deleted.isFalse(),
                                  spot.landmark.id.isNull())
                           .fetch();
    }

    public boolean existsSpotByMemberAndLandmark(Long memberId, Long landmarkId) {
        return Optional.ofNullable(
                queryFactory.select(spot.id)
                            .from(spot)
                            .where(spot.createdDateTime.after(TimeUtil.getThisWeekStartDateTime()),
                                   spot.member.id.eq(memberId),
                                   spot.landmark.id.eq(landmarkId))
                            .fetchOne()
        ).isPresent();
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

    private BooleanExpression gtLandmarkOffset(Long lastOffset) {
        if (lastOffset == null) {
            return null;
        }

        return spot.landmark.id.gt(lastOffset);
    }

}
