package com.bird.maru.common.config.WebSocket;

import com.bird.maru.auctionlog.repository.query.AuctionLogCustomQueryRepository;
import com.bird.maru.domain.model.entity.AuctionLog;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebSocketServiceImpl implements WebSocketService {

    private final AuctionLogCustomQueryRepository auctionLogCustomQueryRepository;

    @Override
    public Bid maxBidPrice(Long landmarkId) {
        Optional<AuctionLog> auctionLog = auctionLogCustomQueryRepository.findFirstByLandmarkId(landmarkId);
        if (auctionLog.isPresent()) {
            return new Bid(auctionLog.get().getPrice(), landmarkId);
        }
        return new Bid(10000, landmarkId);
    }

}
