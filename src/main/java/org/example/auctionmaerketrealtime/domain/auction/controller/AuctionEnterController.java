package org.example.auctionmaerketrealtime.domain.auction.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.auctionmaerketrealtime.domain.auction.service.AuctionEnterService;
import org.example.auctionmaerketrealtime.websocket.BidMessage;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AuctionEnterController {
    private final AuctionEnterService auctionEnterService;

    @MessageMapping("/auction/{auctionId}/enter")
    public void enterAuction(@DestinationVariable Long auctionId,
                             BidMessage bidMessage,
                             SimpMessageHeaderAccessor accessor) {
       auctionEnterService.enterAuction(auctionId,bidMessage,accessor);
    }
}
