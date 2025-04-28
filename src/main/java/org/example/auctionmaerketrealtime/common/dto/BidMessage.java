package org.example.auctionmaerketrealtime.common.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BidMessage {
    private Long auctionId;
    private Long consumerId;
    private String username;
    private Long amount;
    private String type;
}
