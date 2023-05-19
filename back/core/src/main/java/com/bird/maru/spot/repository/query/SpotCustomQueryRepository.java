package com.bird.maru.spot.repository.query;

import static com.bird.maru.domain.model.entity.QLike.like;
import static com.bird.maru.domain.model.entity.QScrap.scrap;
import static com.bird.maru.domain.model.entity.QSpot.spot;
import static com.bird.maru.domain.model.entity.QSpotHasTag.spotHasTag;
import static com.bird.maru.domain.model.entity.QTag.tag;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

import com.bird.maru.cluster.geo.BoundingBox;
import com.bird.maru.auction.controller.dto.AuctionSearchCondition;
import com.bird.maru.cluster.geo.Marker;
import com.bird.maru.common.util.TimeUtil;
import com.bird.maru.domain.model.entity.Spot;
import com.bird.maru.domain.model.entity.Tag;
import com.bird.maru.domain.model.type.MapFilterType;
import com.bird.maru.map.controller.dto.MapCondition;
import com.bird.maru.spot.controller.dto.SpotDetailResponseDto;
import com.bird.maru.spot.controller.dto.SpotMapCondition;
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
        return queryFactory.selectDistinct(spot)
                           .from(spot)
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

//    public List<Marker> findMarkerByBoundingBox(BoundingBox boundingBox) {
//        return queryFactory.select(Projections.fields(Marker.class,
//                                                      spot.id.as("id"),
//                                                      spot.member.id.as("memberId"),
//                                                      spot.coordinate.as("coordinate")
//                           ))
//                           .from(spot)
//                           .where(spot.coordinate.lng.between(boundingBox.getWest(), boundingBox.getEast()),
//                                  spot.coordinate.lat.between(boundingBox.getSouth(), boundingBox.getNorth()),
//                                  spot.deleted.isFalse(),
//                                  spot.landmark.id.isNull())
//                           .fetch();
//    }

    public List<Marker> findMarkerByBoundingBoxWithCondition(MapCondition condition, Long memberId) {
        BoundingBox boundingBox = condition.getBoundingBox();
        MapFilterType filter = condition.getFilter();
        Long tagId = condition.getTagId();
        return queryFactory.select(Projections.fields(Marker.class,
                                                      spot.id.as("id"),
                                                      spot.coordinate.as("coordinate")))
                           .distinct()
                           .from(spot)
                           .leftJoin(spot.tags, spotHasTag)
                           .where(isMine(filter, memberId),
                                  spot.deleted.isFalse(),
                                  spot.landmark.id.isNull(),
                                  containTagId(tagId),
                                  containsLng(boundingBox.getWest(), boundingBox.getEast()),
                                  containsLat(boundingBox.getSouth(), boundingBox.getNorth()))
                           .orderBy(spot.id.desc())
                           .limit(condition.getSize())
                           .fetch();
    }

    public boolean existsSpotByMemberAndLandmark(Long memberId, Long landmarkId) {
        return Optional.ofNullable(
                queryFactory.select(spot.id)
                            .from(spot)
                            .where(spot.createdDateTime.after(TimeUtil.getThisWeekStartDateTime()),
                                   spot.member.id.eq(memberId),
                                   spot.landmark.id.eq(landmarkId))
                            .fetchFirst()
        ).isPresent();
    }

    public Optional<SpotDetailResponseDto> findSpotDetail(Long spotId, Long memberId) {
        return Optional.ofNullable(
                queryFactory.select(Projections.fields(SpotDetailResponseDto.class,
                                                       Expressions.asNumber(spotId).as("id"),
                                                       spot.coordinate.as("coordinate"),
                                                       Expressions.asNumber(spot.landmark.id != null ? spot.landmark.id : null).as("landmarkId"),
                                                       spot.image.url.as("imageUrl"),
                                                       Expressions.asBoolean(scrap.spot.id.isNotNull()).as("scraped"),
                                                       Expressions.asBoolean(like.spot.id.isNotNull()).as("liked"),
                                                       spot.likeCount.as("likeCount")
                            ))
                            .from(spot)
                            .leftJoin(scrap).on(spot.id.eq(scrap.spot.id),
                                                scrap.member.id.eq(memberId),
                                                scrap.deleted.isFalse())
                            .leftJoin(like).on(spot.id.eq(like.spot.id),
                                               like.member.id.eq(memberId),
                                               like.deleted.isFalse())
                            .where(spot.id.eq(spotId),
                                   spot.deleted.isFalse()
                            ).fetchOne());
    }

    public List<SpotSimpleDto> findSpotBasedMap(List<Long> spotIds) {
        return queryFactory.selectFrom(spotHasTag)
                           .join(tag).on(spotHasTag.tag.id.eq(tag.id))
                           .rightJoin(spot).on(spotHasTag.spot.id.eq(spot.id))
                           .where(spot.id.in(spotIds))
                           .orderBy(spot.id.desc())
                           .transform(groupBy(spot.id).list(Projections.constructor(SpotSimpleDto.class,
                                                                                    spot.id.as("id"),
                                                                                    spot.image.url.as("imageUrl"),
                                                                                    list(Projections.constructor(Tag.class,
                                                                                                                 tag.id,
                                                                                                                 tag.name))
                           )));
    }

    public List<Long> findIdsBasedMap(SpotMapCondition condition, Long memberId) {
        return queryFactory.selectDistinct(spot.id)
                           .from(spotHasTag)
                           .join(tag).on(spotHasTag.tag.id.eq(tag.id))
                           .rightJoin(spot).on(spotHasTag.spot.id.eq(spot.id))
                           .where(ltSpotOffset(condition.getLastOffset()),
                                  eqTagId(condition.getTagId()),
                                  containsLng(condition.getWest(), condition.getEast()),
                                  containsLat(condition.getSouth(), condition.getNorth()),
                                  isMine(condition.getFilter(), memberId),
                                  spot.deleted.isFalse(),
                                  spot.landmark.id.isNull())
                           .orderBy(spot.id.desc())
                           .limit(condition.getSize())
                           .fetch();
    }

    private BooleanExpression isMine(MapFilterType filterType, Long memberId) {
        return MapFilterType.ALL.equals(filterType) ? null : spot.member.id.eq(memberId);
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

    private BooleanExpression containTagId(Long tagId) {
        return tagId == null ? null : spotHasTag.tag.id.eq(tagId);
    }

    private BooleanExpression eqTagId(Long tagId) {
        return tagId == null ? null : tag.id.eq(tagId);
    }

    private BooleanExpression containsLng(Double west, Double east) {
        return spot.coordinate.lng.between(west, east);
    }

    private BooleanExpression containsLat(Double south, Double north) {
        return spot.coordinate.lat.between(south, north);
    }

}
