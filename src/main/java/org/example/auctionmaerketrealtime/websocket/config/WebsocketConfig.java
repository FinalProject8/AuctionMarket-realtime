package org.example.auctionmaerketrealtime.websocket.config;

import lombok.RequiredArgsConstructor;
import org.example.auctionmaerketrealtime.websocket.AuctionWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-stomp")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

//    private final AuctionWebSocketHandler auctionWebSocketHandler;
//
//    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        registry.addHandler(auctionWebSocketHandler, "/ws/auction")
//                .setAllowedOrigins("*")
//                .withSockJS();
//    }
//
////    @Override
////    public void configureMessageBroker(MessageBrokerRegistry registry) {
////        registry.enableSimpleBroker("/topic");
////
////        registry.setApplicationDestinationPrefixes("/app");
////    }
}
