package com.bird.maru.common.config.WebSocket;

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

    private final WebSocketService webSocketService;
    private final SimpMessageSendingOperations messagingTemplate;
    ArrayList<String> users = new ArrayList<>();

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        log.info("새로운 사용자 웹 소켓 연결");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
//        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
//        String username = (String) headerAccessor.getSessionAttributes().get("username");
        log.info("사용자가 웹 소켓 연결을 끊었습니다.");
    }

    /**
     * /app/bid로 요청이 들어오면 해당 메소드로 처리 -> 프런트에게 전달
     */
    @MessageMapping("/bid")
    @SendTo("/bidding/price")
    public Bid updateBid(@RequestBody Bid bid) {
        log.info("websocket 실질적 보냄 !!! : {} {}", bid.getPrice(), bid.getLandmarkId());
        return webSocketService.maxBidPrice(bid.getLandmarkId());
    }

}
