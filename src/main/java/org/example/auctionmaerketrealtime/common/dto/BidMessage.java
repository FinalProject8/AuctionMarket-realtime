package org.example.auctionmaerketrealtime.common.dto;

import lombok.*;

@Getter
@NoArgsConstructor
public class BidMessage {
    private Long auctionId;
    private Long consumerId;
    private String username;
    private Long amount;
    private String type;

    @Builder
    public BidMessage(Long auctionId, Long consumerId, String username, Long amount, String type) {
        this.auctionId = auctionId;
        this.consumerId = consumerId;
        this.username = username;
        this.amount = amount;
        this.type = type;
    }
}
