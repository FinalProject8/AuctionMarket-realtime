package org.example.auctionmaerketrealtime.domain.auction.controller;

import lombok.RequiredArgsConstructor;
import org.example.auctionmaerketrealtime.domain.auction.service.AuctionEndService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/auction/{auctionId}")
public class AuctionEndController {
    private final AuctionEndService auctionEndService;

    @PostMapping("/winner")
    public ResponseEntity<Void> sendWinner(@PathVariable Long auctionId) {
        auctionEndService.sendWinner(auctionId);
        return ResponseEntity.ok().build();
    }
}
