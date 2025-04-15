package org.example.auctionmaerketrealtime.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AuctionWebSocketHandler extends TextWebSocketHandler {
    private final Set<WebSocketSession> sessions = new HashSet<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);

        System.out.println(session.getId() + "님이 연결되었습니다");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String payload = message.getPayload();
//        System.out.println(message.getPayload());

        if (!payload.matches("\\d+")) {
            session.sendMessage(new TextMessage("잘못된 입찰가입니다. 숫자만 입력하세요"));
            return;
        }

        for (WebSocketSession connectedSession : sessions) {
            connectedSession.sendMessage(new TextMessage("입찰가 :" + payload + "원"));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);

        System.out.println(session.getId() + "님의 연결이 해제되었습니다");
    }

}
