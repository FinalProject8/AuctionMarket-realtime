package org.example.auctionmaerketrealtime.domain.bid.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
//import org.example.auctionmaerketrealtime.domain.auction.service.AuctionEnterService;
import org.example.auctionmaerketrealtime.domain.bid.service.BidService;
import org.example.auctionmaerketrealtime.websocket.BidMessage;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.Duration;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AuctionStompController {

    private final BidService bidService;
//    private final AuctionEnterService auctuinEnterService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/auction/{auctionId}/bid")
    public void handleBid(@DestinationVariable Long auctionId, BidMessage bidMessage) {
        log.info("💾 입찰 저장: {}님 {}원 (경매 {})", bidMessage.getUsername(), bidMessage.getAmount(), auctionId);

        BidMessage save = bidService.placeBid(auctionId, bidMessage);

        messagingTemplate.convertAndSend("/topic/auction/" + auctionId, save);
    }

//    @MessageMapping("/auction/{auctionId}/enter")
//    public void enterAuction(
//            @DestinationVariable Long auctionId,
//            BidMessage bidMessage,
//            SimpMessageHeaderAccessor accessor) {
//        auctuinEnterService.enterAuction(auctionId, bidMessage, accessor);
//    }
}
