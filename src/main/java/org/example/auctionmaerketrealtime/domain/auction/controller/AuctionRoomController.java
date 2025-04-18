package org.example.auctionmaerketrealtime.domain.auction.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.auctionmaerketrealtime.domain.auction.dto.WebSocketAuctionCreateRequest;
import org.example.auctionmaerketrealtime.domain.auction.dto.WebSocketAuctionCreateResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/auction")
public class AuctionRoomController {

    @PostMapping("/start")
    public ResponseEntity<WebSocketAuctionCreateResponse> startAuctionRoom(
            @RequestBody WebSocketAuctionCreateRequest request) {
        String roomUrl = "http://localhost:8081/auction.html?auctionId=" + request.getAuctionId();

        log.info("경매방 생성완료 : {}", roomUrl);

        WebSocketAuctionCreateResponse response = new WebSocketAuctionCreateResponse(roomUrl);
        return ResponseEntity.ok(response);
    }
}
