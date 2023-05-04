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

    /**
     * 이번주에 방문하지 않은 경매 목록을 조회합니다.
     *
     * @param memberId           현재 로그인 한 회원의 ID
     * @param visitedLandmarkIds 이번주에 방문했던 랜드마크 ID 목록
     * @return 이번주에 방문하지 않은 경매 목록
     */
    public List<Auction> findAllByMemberAndLandmarkIdIn(Long memberId, List<Long> visitedLandmarkIds, int size) {
        return queryFactory.selectFrom(auction)
                           .join(auction.landmark, landmark).fetchJoin()
                           .where(
                                   auction.finished.isFalse(),
                                   auction.landmark.id.in(visitedLandmarkIds),
                                   queryNotExistAuctionLog(memberId, visitedLandmarkIds)
                           )
                           .orderBy(auction.landmark.id.asc())
                           .limit(size)
                           .fetch();
    }

    private BooleanExpression queryNotExistAuctionLog(Long memberId, List<Long> landmarkIds) {
        return auction.notIn(
                JPAExpressions.select(auctionLog.auction)
                              .from(auctionLog)
                              .join(auctionLog.auction, auction)
                              .where(
                                      auctionLog.modifiedDateTime.after(TimeUtil.getCurrentAuctionStartDateTime()),
                                      auctionLog.member.id.eq(memberId),
                                      auctionLog.auction.landmark.id.in(landmarkIds)
                              )
        );
    }

}
