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
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SpotCustomQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<Long> findIdsByCondition(Long memberId, SpotSearchCondition condition) {
        return queryFactory.select(spot.id)
                           .from(spot)
                           .where(
                                   spot.deleted.eq(Boolean.FALSE),
                                   ltOffset(condition.getLastOffset()),
                                   isMine(condition.getMine(), memberId),
                                   isScraped(condition.getScraped(), memberId)
                           )
                           .orderBy(offsetOrder())
                           .limit(condition.getSize())
                           .fetch();
    }

    public List<Spot> findAllWithTagsByIdIn(List<Long> spotIds) {
        return queryFactory.selectFrom(spot)
                           .join(spot.tags, spotHasTag).fetchJoin()
                           .join(spotHasTag.tag, tag).fetchJoin()
                           .where(spot.id.in(spotIds))
                           .orderBy(offsetOrder())
                           .fetch();
    }

    private BooleanExpression ltOffset(Long lastOffset) {
        if (lastOffset == null) {
            return null;
        }

        return spot.id.lt(lastOffset);
    }

    private BooleanExpression isMine(Boolean myId, Long memberId) {
        if (myId == null || myId.equals(Boolean.FALSE)) {
            return null;
        }

        return spot.member.id.eq(memberId);
    }

    private BooleanExpression isScraped(Boolean scraped, Long memberId) {
        if (scraped == null || scraped.equals(Boolean.FALSE)) {
            return null;
        }

        return spot.id.in(
                JPAExpressions.select(scrap.spot.id)
                              .from(scrap)
                              .where(scrap.deleted.eq(Boolean.FALSE),
                                     scrap.member.id.eq(memberId))
        );
    }

    private OrderSpecifier<?>[] offsetOrder() {
        return List.of(
                spot.id.desc()
//                spot.createdDateTime.desc()
        ).toArray(new OrderSpecifier<?>[0]);
    }

}
