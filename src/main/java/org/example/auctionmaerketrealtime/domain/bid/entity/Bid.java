package org.example.auctionmaerketrealtime.domain.bid.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.auctionmaerketrealtime.domain.auction.entity.Auction;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long consumerId;
    private String username;
    private Long amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aution_id")
    private Auction auction;

    private LocalDateTime createdAt;
}
