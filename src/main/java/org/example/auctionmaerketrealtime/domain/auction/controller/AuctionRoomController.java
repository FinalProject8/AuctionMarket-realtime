package org.example.auctionmaerketrealtime.domain.auction.controller;

import lombok.RequiredArgsConstructor;
import org.example.auctionmaerketrealtime.domain.auction.dto.WebSocketAuctionCreateRequest;
import org.example.auctionmaerketrealtime.domain.auction.dto.WebSocketAuctionCreateResponse;
import org.example.auctionmaerketrealtime.domain.auction.service.AuctionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/auction")
public class AuctionRoomController {

    private final AuctionService auctionService;

    // 경매방 생성
    @PostMapping("/start")
    public ResponseEntity<WebSocketAuctionCreateResponse> startAuctionRoom(@RequestBody WebSocketAuctionCreateRequest request) {
        WebSocketAuctionCreateResponse response = auctionService.createAuction(request);
        return ResponseEntity.ok(response);
    }

    // 유저별 입장 html 생성
    @PostMapping("/join")
    public ResponseEntity<WebSocketAuctionCreateResponse> joinAuctionRoom(@RequestBody WebSocketAuctionCreateRequest request) {
        WebSocketAuctionCreateResponse response = auctionService.generateJoinUrl(request);
        return ResponseEntity.ok(response);
    }
}
