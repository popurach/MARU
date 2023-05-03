package com.bird.maru.auction.repository.query;

import static com.bird.maru.domain.model.entity.QAuction.auction;
import static com.bird.maru.domain.model.entity.QAuctionLog.auctionLog;
import static com.bird.maru.domain.model.entity.QLandmark.landmark;

import com.bird.maru.common.util.TimeUtil;
import com.bird.maru.domain.model.entity.Auction;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AuctionCustomQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<Auction> findAllByMemberAndLandmarkIdIn(Long memberId, List<Long> visitedLandmarkIds) {
        return queryFactory.selectFrom(auction)
                           .join(auction.landmark, landmark).fetchJoin()
                           .where(
                                   auction.finished.isFalse(),
                                   auction.landmark.id.in(visitedLandmarkIds),
                                   queryNotExistAuctionLog(memberId, visitedLandmarkIds)
                           )
                           .orderBy(auction.landmark.id.asc())
                           .fetch();
    }

    private BooleanExpression queryNotExistAuctionLog(Long memberId, List<Long> landmarkIds) {
        return auction.notIn(
                JPAExpressions.select(auctionLog.auction)
                              .from(auctionLog)
                              .where(
                                      auctionLog.modifiedDateTime.after(TimeUtil.getCurrentAuctionStartDateTime()),
                                      auctionLog.member.id.eq(memberId),
                                      auctionLog.auction.landmark.id.in(landmarkIds)
                              )
        );
    }

}
