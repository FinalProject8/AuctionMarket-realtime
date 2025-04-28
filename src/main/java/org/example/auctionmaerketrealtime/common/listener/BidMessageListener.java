package org.example.auctionmaerketrealtime.common.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.auctionmaerketrealtime.common.dto.BidMessage;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class BidMessageListener implements MessageListener {

    private final ObjectMapper objectMapper;

    public static final Map<String, Set<WebSocketSession>> auctionSessions = new ConcurrentHashMap<>();

    @Override
    public void onMessage(Message message, byte[] patten) {
        try {
            String channel = new String(message.getChannel());
            String auctionId = channel.split(":")[2];

            String body = new String(message.getBody());
            log.info("[Redis PubSub] auctionId: {}, body: {}",auctionId, body);

            BidMessage bidMessage = objectMapper.readValue(body, BidMessage.class);

            Set<WebSocketSession> sessions = auctionSessions.getOrDefault(auctionId, Set.of());
            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(
                            bidMessage.getUsername() + "님이 " + bidMessage.getAmount() + "원 입찰!"
                    ));
                }
            }
            log.info("입찰 브로드캐스트 완료 (경매ID: {})",auctionId);
        } catch (Exception e) {
            log.error("Redis 메세지 처리 실패",e);
        }
    }
}
