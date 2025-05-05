package org.example.auctionmaerketrealtime.domain.bid.controller;

import lombok.RequiredArgsConstructor;
import org.example.auctionmaerketrealtime.domain.bid.service.BidService;
import org.example.auctionmaerketrealtime.common.dto.BidMessage;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class AuctionStompController {

    private final BidService bidService;
    private final SimpMessagingTemplate messagingTemplate;

    // 유저 입장 메세지
    @MessageMapping("/auction/{auctionId}/enter")
    public void handleEnter(@DestinationVariable Long auctionId, BidMessage bidMessage) {
        messagingTemplate.convertAndSend("/topic/auction/" + auctionId,
                bidService.buildEnterMessage(bidMessage));
    }

    // 유저 입찰
    @MessageMapping("/auction/{auctionId}/bid")
    public void handleBid(@DestinationVariable Long auctionId, BidMessage bidMessage) {
        BidMessage save = bidService.placeBid(auctionId, bidMessage);
        messagingTemplate.convertAndSend("/topic/auction/" + auctionId,
                bidService.buildBidResponse(save));
    }
}
