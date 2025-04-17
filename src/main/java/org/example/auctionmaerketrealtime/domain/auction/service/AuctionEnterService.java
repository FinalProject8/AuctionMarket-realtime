package org.example.auctionmaerketrealtime.domain.auction.service;

import lombok.RequiredArgsConstructor;
import org.example.auctionmaerketrealtime.domain.auction.entity.Auction;
import org.example.auctionmaerketrealtime.domain.auction.repository.AuctionRepository;
import org.example.auctionmaerketrealtime.websocket.BidMessage;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuctionEnterService {
    private final AuctionRepository auctionRepository;
    private final SimpMessagingTemplate messagingTemplate;

    private final Duration enterLimit = Duration.ofMinutes(10);

    public void enterAuction(Long auctionId, BidMessage bidMessage, SimpMessageHeaderAccessor accessor) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new RuntimeException("Auction not found"));

        if (auction.getStartTime().minus(enterLimit).isBefore(LocalDateTime.now())) {
            messagingTemplate.convertAndSendToUser(
                    accessor.getSessionId(), "/queue/errors", "입장시간이 지났습니다"
            );
            return;
        }

        accessor.getSessionAttributes().put("username", bidMessage.getUsername());
        accessor.getSessionAttributes().put("auctionId", auctionId);

        messagingTemplate.convertAndSend("/topic/auction/" + auctionId,
                Map.of("type", "ENTER", "username", bidMessage.getUsername()));
    }
}
