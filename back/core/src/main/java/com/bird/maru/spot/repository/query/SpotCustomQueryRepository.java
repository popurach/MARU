package com.bird.maru.spot.repository.query;

import static com.bird.maru.domain.model.entity.QScrap.scrap;
import static com.bird.maru.domain.model.entity.QSpot.spot;
import static com.bird.maru.domain.model.entity.QSpotHasTag.spotHasTag;
import static com.bird.maru.domain.model.entity.QTag.tag;

import com.bird.maru.domain.model.entity.Spot;
import com.bird.maru.spot.controller.dto.SpotSearchCondition;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
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
                                   ltOffset(memberId, condition)
                           )
                           .orderBy(offsetOrder(condition))
                           .limit(condition.getSize())
                           .fetch();
    }

    public List<Spot> findAllWithTagsByIdIn(List<Long> spotIds) {
        return queryFactory.selectFrom(spot)
                           .join(spot.tags, spotHasTag).fetchJoin()
                           .join(spotHasTag.tag, tag).fetchJoin()
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
                                   ltOffset(memberId, condition)
                           )
                           .orderBy(offsetOrder(condition))
                           .limit(condition.getSize())
                           .fetch();
    }

    private BooleanExpression ltOffset(Long memberId, SpotSearchCondition condition) {
        if (condition.getLastOffset() == null) {
            return null;
        }

        if (Boolean.TRUE.equals(condition.getMine())) {
            return ltMineOffset(condition.getLastOffset());
        }

        if (Boolean.TRUE.equals(condition.getScraped())) {
            return ltScrapOffset(memberId, condition.getLastOffset());
        }

        return null;
    }

    private BooleanExpression ltMineOffset(Long lastOffset) {
        return spot.id.lt(lastOffset);
    }

    private BooleanExpression ltScrapOffset(Long memberId, Long lastOffset) {
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
