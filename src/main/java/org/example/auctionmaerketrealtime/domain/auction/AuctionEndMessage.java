package org.example.auctionmaerketrealtime.domain.auction;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuctionEndMessage {
    private Long auctionId;
    private String winnerName;
    private Long amount;
    private String comment;
}
