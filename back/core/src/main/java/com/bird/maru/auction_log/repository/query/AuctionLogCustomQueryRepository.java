package com.bird.maru.auction_log.repository.query;

import static com.bird.maru.domain.model.entity.QAuction.auction;
import static com.bird.maru.domain.model.entity.QAuctionLog.auctionLog;

import com.bird.maru.domain.model.entity.Auction;
import com.bird.maru.domain.model.entity.AuctionLog;
import com.querydsl.jpa.impl.JPAQueryFactory;
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

}
