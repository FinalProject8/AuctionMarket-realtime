package org.example.auctionmaerketrealtime.common.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.auctionmaerketrealtime.common.dto.BidMessage;
import org.example.auctionmaerketrealtime.common.listener.BidMessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.URI;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuctionWebSocketHandler extends TextWebSocketHandler {
    private final RedisTemplate<String, Object> redisPubSubTemplate;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String auctionId = extractAuctionId(session);
        BidMessageListener.auctionSessions
                .computeIfAbsent(auctionId, k -> ConcurrentHashMap.newKeySet())
                .add(session);

        System.out.println("[접속] " + session.getId() +  "님이 " + auctionId + "경매방에 입장했습니다");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String payload = message.getPayload();
        String auctionId = extractAuctionId(session);

        if (!payload.matches("\\d+")) {
            try {
                session.sendMessage(new TextMessage("잘못된 입찰가입니다. 숫자만 입력하세요"));
            } catch (IOException e) {
                log.warn("메세지 전송 실패", e);
            }
            return;
        }

        try {
            BidMessage bidMessage = new BidMessage();
            bidMessage.setAuctionId(Long.parseLong(auctionId));
            bidMessage.setAmount(Long.parseLong(payload));
            bidMessage.setUsername("익명");

            redisPubSubTemplate.convertAndSend("auction:bid", bidMessage);
            log.info("[Redis 입찰 발행] {}원 - 경매: {}", bidMessage.getAmount(), auctionId);
        } catch (Exception e) {
            log.error("입찰 처리 중 예외 발생", e);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String auctionId = extractAuctionId(session);
        Set<WebSocketSession> sessions = BidMessageListener.auctionSessions.get(auctionId);

        if (sessions != null) {
            sessions.remove(session);
            System.out.println("[퇴장] " + session.getId() + "님이 " + auctionId + "경매방에서 나갔습니다");

            if (sessions.isEmpty()) {
                BidMessageListener.auctionSessions.remove(auctionId);
            }
        }
    }

    private String extractAuctionId(WebSocketSession session) {
        URI uri = session.getUri();
        if (uri == null) {
            return "unknown";
        }
        String[] segments = uri.getPath().split("/");
        return segments.length > 0 ? segments[segments.length - 1] : "unknown";
    }
}
