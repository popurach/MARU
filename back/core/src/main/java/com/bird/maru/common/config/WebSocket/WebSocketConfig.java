package com.bird.maru.common.config.WebSocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
/**
 * 특정 랜드마크에 대한 최대 입찰 가격을 주고 받는 Stomp 웹소켓
 * */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 해당 주소를 구독하고 있는 사용자들에게 메시지 전달
        // api에 /bidding이 붙을 경우 messageBroker가 해당 경로를 가로챔
        registry.enableSimpleBroker("/bidding");
        // 사용자들이 보낸 메시지를 받을 prefix
        registry.setApplicationDestinationPrefixes("/bid");
    }
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/socket.io")
                .setAllowedOrigins("*")
                .withSockJS();

        registry.addEndpoint("/socket")
                .setAllowedOrigins("*");
        // endpoint 주소 : wss://k8a403.p.ssafy.io/socket
    }
}
