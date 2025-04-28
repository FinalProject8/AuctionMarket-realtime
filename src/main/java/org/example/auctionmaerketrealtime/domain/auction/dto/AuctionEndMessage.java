package org.example.auctionmaerketrealtime.domain.auction.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuctionEndMessage {
    private Long auctionId;
    private Long consumerId;
    private Long amount;
}
