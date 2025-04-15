package org.example.auctionmaerketrealtime.websocket;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BidMessage {
    private String username;
    private Long amount;
}
