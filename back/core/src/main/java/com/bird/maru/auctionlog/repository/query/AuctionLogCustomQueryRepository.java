package com.bird.maru.auctionlog.repository.query;

import static com.bird.maru.domain.model.entity.QAuction.auction;
import static com.bird.maru.domain.model.entity.QAuctionLog.auctionLog;

import com.bird.maru.domain.model.entity.Auction;
import com.bird.maru.domain.model.entity.AuctionLog;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AuctionLogCustomQueryRepository {

    private JPAQueryFactory queryFactory;

    public Optional<AuctionLog> findWithAuctionById(Long auctionLogId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(auctionLog)
                            .join(auctionLog.auction, auction).fetchJoin()
                            .where(auctionLog.id.eq(auctionLogId))
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
//                .join(auction).on(auctionLog.auction.eq(auction))
        return queryFactory.selectFrom(auctionLog)
                .join(auctionLog.auction, auction).on(auctionLog.id.eq(auction.lastLogId))
//                .where(auction.landmark.id.eq(landmarkId),
                .where(eqLandmark(landmarkId),
                       auction.finished.isTrue())
                .orderBy(auction.createdDate.desc())
                .limit(10)
                .fetch();
    }

    public BooleanExpression eqLandmark(Long landmarkId) {
        return landmarkId == null ? null :  auction.landmark.id.eq(landmarkId);
    }


}
