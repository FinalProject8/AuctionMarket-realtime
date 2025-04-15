//package org.example.auctionmaerketrealtime.websocket;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.messaging.handler.annotation.DestinationVariable;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.stereotype.Controller;
//
//@Slf4j
//@Controller
//public class AuctionStompController {
//
//    @MessageMapping("/auction/{auctionId}/bid")
//    @SendTo("/topic/auction/{auctionId}")
//    public BidMessage bidMessage(@DestinationVariable String auctionId, BidMessage bidMessage) {
//        log.info("경매 {}번 | {}: {}원 입찰", auctionId, bidMessage.getUsername(), bidMessage.getAmount());
//
//        return bidMessage;
//    }
//}
