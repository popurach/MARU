package com.bird.maru.auctionlog.repository.query;

import static com.bird.maru.domain.model.entity.QAuction.auction;
import static com.bird.maru.domain.model.entity.QAuctionLog.auctionLog;
import static com.bird.maru.domain.model.entity.QLandmark.landmark;

import com.bird.maru.auctionlog.controller.dto.AuctionLogSearchCondition;
import com.bird.maru.domain.model.entity.Auction;
import com.bird.maru.domain.model.entity.AuctionLog;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
@RequiredArgsConstructor
public class AuctionLogCustomQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<AuctionLog> findAllWithAuctionByMemberAndCondition(Long memberId, AuctionLogSearchCondition condition) {
        return queryFactory.selectFrom(auctionLog)
                           .join(auctionLog.auction, auction).fetchJoin()
                           .join(auction.landmark, landmark).fetchJoin()
                           .where(
                                   auction.finished.isFalse(),
                                   auctionLog.member.id.eq(memberId),
                                   ltOffset(condition.getLastOffset())
                           )
                           .orderBy(auctionLog.modifiedDateTime.desc(), auctionLog.id.desc())
                           .limit(condition.getSize())
                           .fetch();
    }

    public Optional<AuctionLog> findWithAuctionById(Long memberId, Long auctionLogId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(auctionLog)
                            .join(auctionLog.auction, auction).fetchJoin()
                            .where(
                                    auctionLog.member.id.eq(memberId),
                                    auctionLog.id.eq(auctionLogId)
                            )
                            .fetchOne()
        );
    }

    public Optional<AuctionLog> findFirstByAuction(Auction auction) {
        return Optional.ofNullable(
                queryFactory.selectFrom(auctionLog)
                            .where(auctionLog.auction.eq(auction))
                            .orderBy(auctionLog.price.desc())
                            .fetchFirst()
        );
    }

    public List<AuctionLog> auctionRecordTop10(Long landmarkId) {
        return queryFactory.selectFrom(auctionLog)
                           .join(auctionLog.auction, auction).on(auctionLog.id.eq(auction.lastLogId))
                           .where(auction.landmark.id.eq(landmarkId),
                                  auction.finished.isTrue())
                           .orderBy(auctionLog.createdDateTime.asc())
                           .limit(10)
                           .fetch();
    }

    public Optional<AuctionLog> findByLandmarkAndMember(Long memberId, Long landmarkId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(auctionLog)
                            .join(auctionLog.auction, auction)
                            .where(
                                    auction.finished.isFalse(),
                                    auctionLog.member.id.eq(memberId),
                                    auction.landmark.id.eq(landmarkId)
                            )
                            .fetchOne()
        );
    }

    public Optional<AuctionLog> findFirstByLandmarkId(Long landmarkId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(auctionLog)
                            .join(auctionLog.auction, auction).fetchJoin()
                            .where(
                                    auction.finished.isFalse(),
                                    auction.landmark.id.eq(landmarkId)
                            )
                            .orderBy(auctionLog.price.desc())
                            .fetchFirst()
        );
    }

    private BooleanExpression ltOffset(Long lastOffset) {
        if (lastOffset == null) {
            return null;
        }

        return auctionLog.modifiedDateTime.before(queryModifiedDateTimeById(lastOffset))
                                          .or(auctionLog.modifiedDateTime.eq(queryModifiedDateTimeById(lastOffset))
                                                                         .and(auctionLog.id.lt(lastOffset)));
    }

    private JPQLQuery<LocalDateTime> queryModifiedDateTimeById(Long lastOffset) {
        return JPAExpressions.select(auctionLog.modifiedDateTime)
                             .from(auctionLog)
                             .where(auctionLog.id.eq(lastOffset));
    }

}
