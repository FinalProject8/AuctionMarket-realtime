package org.example.auctionmaerketrealtime.domain.auction.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.auctionmaerketrealtime.domain.auction.enums.AuctionStatus;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Auction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private Long topPrice;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "Auction_Status")
    private AuctionStatus status;

    public void end() {
        this.status = AuctionStatus.ENDED;
    }
}
