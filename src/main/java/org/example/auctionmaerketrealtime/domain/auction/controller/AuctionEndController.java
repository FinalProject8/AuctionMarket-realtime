package org.example.auctionmaerketrealtime.domain.auction.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.auctionmaerketrealtime.domain.auction.service.AuctionEndService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/auction")
public class AuctionEndController {
    private final AuctionEndService auctionEndService;

    @PostMapping("/{auctionId}/winner")
    public ResponseEntity<Void> sendWinner(@PathVariable Long auctionId) {
        log.info("[요청] 경매 종료 - 메인 서버로 낙찰자 정보 전송 auctionId: {}", auctionId);
        auctionEndService.sendWinner(auctionId);
        return ResponseEntity.ok().build();
    }
}
