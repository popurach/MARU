package com.bird.maru.common.config.WebSocket;

import com.bird.maru.auctionlog.repository.query.AuctionLogCustomQueryRepository;
import com.bird.maru.domain.model.entity.AuctionLog;
import java.util.ArrayList;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@RestController
@RequiredArgsConstructor
@Slf4j
public class WebSocketController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final AuctionLogCustomQueryRepository auctionLogCustomQueryRepository;
    ArrayList<String> users = new ArrayList<>();

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        log.info("새로운 사용자가 웹 소켁을 연결");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");

        if (username != null) {
            log.info("유저 Disconnected : {}", username);

            users.remove(username);
            ChatMessageDTO chat = new ChatMessageDTO(MessageType.LEAVE, null, username);
            messagingTemplate.convertAndSend("/roomname/public", chat);
        }
    }

    /**
     * /app/bid로 요청이 들어오면 해당 메소드로 처리 -> 프런트에게 전달
     */
    @MessageMapping("/bid")
    @SendTo("/bidding/price")
    public Bid updateBid(@RequestBody Bid bid) {
        log.info("요청 들어옴 !!! {} {}", bid.getPrice(), bid.getLandmarkId());
        Optional<AuctionLog> auctionLog = auctionLogCustomQueryRepository.findFirstByLandmarkId(bid.getLandmarkId());
        if (auctionLog.isPresent()) {
            return new Bid(auctionLog.get().getPrice(), bid.getLandmarkId());
        }
        return new Bid(10000, bid.getLandmarkId());
    }

}
