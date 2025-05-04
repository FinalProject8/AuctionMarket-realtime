package org.example.auctionmaerketrealtime.common.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.auctionmaerketrealtime.common.dto.BidMessage;
import org.example.auctionmaerketrealtime.common.exception.InternalErrorCode;
import org.example.auctionmaerketrealtime.common.exception.InternalException;
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
    public void onMessage(Message message, byte[] pattern) {
        String channel = new String(message.getChannel());
        String[] parts = channel.split(":");
        if (parts.length < 3) {
            throw new InternalException(InternalErrorCode.SESSION_URI_MISSING);
        }
        String auctionId = parts[2];

        String body = new String(message.getBody());
        if (body == null || body.isBlank()) {
            throw new InternalException(InternalErrorCode.INVALID_BID_FORMAT);
        }

        BidMessage bidMessage = parseBidMessage(body);

        Set<WebSocketSession> sessions = auctionSessions.getOrDefault(auctionId, Set.of());
        for (WebSocketSession session : sessions) {
            if (!session.isOpen()) continue;
            sendMessage(session, bidMessage);
        }
    }

    private BidMessage parseBidMessage(String body) {
        BidMessage bidMessage = objectMapper.convertValue(body, BidMessage.class);
        if (bidMessage.getUsername() == null || bidMessage.getAmount() == null) {
            throw new InternalException(InternalErrorCode.INVALID_BID_FORMAT);
        }
        return bidMessage;
    }

    private void sendMessage(WebSocketSession session, BidMessage bidMessage) {
        if (!session.isOpen()) {
            throw new InternalException(InternalErrorCode.SESSION_CLOSED);
        }

        try {
            session.sendMessage(new TextMessage(
                    bidMessage.getUsername() + "님이 " + bidMessage.getAmount() + "원 입찰!"));
        } catch (Exception e) {
            throw new InternalException(InternalErrorCode.MESSAGE_SEND_FAILED);
        }
    }
}
