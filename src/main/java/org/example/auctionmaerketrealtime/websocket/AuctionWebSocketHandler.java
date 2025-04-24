package org.example.auctionmaerketrealtime.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class AuctionWebSocketHandler extends TextWebSocketHandler {
    private final Map<String, Set<WebSocketSession>> auctionSessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String auctionId = extractAuctionId(session);
        auctionSessions
                .computeIfAbsent(auctionId, k -> ConcurrentHashMap.newKeySet())
                .add(session);

        System.out.println("[접속] " + session.getId() +  "님이 " + auctionId + "경매방에 입장했습니다");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String payload = message.getPayload();
        String auctionId = extractAuctionId(session);

        if (!payload.matches("\\d+")) {
            session.sendMessage(new TextMessage("잘못된 입찰가입니다. 숫자만 입력하세요"));
            return;
        }

        Set<WebSocketSession> sessions = auctionSessions.getOrDefault(auctionId, Set.of());
        for (WebSocketSession connectedSession : sessions) {
            if (connectedSession.isOpen()) {
                connectedSession.sendMessage(new TextMessage("입찰가 : " + payload + '원'));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String auctionId = extractAuctionId(session);
        Set<WebSocketSession> sessions = auctionSessions.get(auctionId);

        if (sessions != null) {
            sessions.remove(session);
            System.out.println("[퇴장] " + session.getId() + "님이 " + auctionId + "경매방에서 나갔습니다");

            if (sessions.isEmpty()) {
                auctionSessions.remove(auctionId);
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
