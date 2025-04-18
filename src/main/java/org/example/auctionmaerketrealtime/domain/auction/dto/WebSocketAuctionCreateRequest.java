package org.example.auctionmaerketrealtime.domain.auction.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class WebSocketAuctionCreateRequest {
    private Long auctionId;
    private String productName;
    private Long minPrice;
    private LocalDateTime endTime;
}
