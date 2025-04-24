package org.example.auctionmaerketrealtime.domain.auction.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.auctionmaerketrealtime.domain.auction.AuctionEndMessage;
import org.example.auctionmaerketrealtime.domain.auction.service.AuctionEndService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/auction")
public class AuctionWinnerController {
    private final AuctionEndService auctionEndService;

    // 스케줄링 서버에서 낙찰자 정보를 확인
    @GetMapping("/{auctionId}/winner")
    public ResponseEntity<AuctionEndMessage> getWinner(@PathVariable Long auctionId) {
        log.info("[요청] 경매가 종료되어 낙찰자 정보요청이 들어왔습니다 auctionId: {}", auctionId);
        return ResponseEntity.ok(auctionEndService.getWinnerInfo(auctionId));
    }
}
