package org.example.auctionmaerketrealtime.websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {

    // client 가 server 와 웹소캣을 연결할 엔드포인트 지정
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-stomp")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    // 메세지 브로커 동작 설정
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // client 가 서버로 요청하는 엔드포인트는 아래에서 지정한 /sub 으로 시작해야 한다
        config.setApplicationDestinationPrefixes("/pub"); // client > server 요청 들어올 prefix 경로
        // client 는 아래에서 지정한 prefix 를 시작으로 경로를 구독 해야한다
        config.enableSimpleBroker("/topic", "queue"); // client 가 구독(sub)할 prefix 경로

    }
}
