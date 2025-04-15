package org.example.auctionmaerketrealtime.domain.bid.repository;

import org.example.auctionmaerketrealtime.domain.bid.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BidRepository extends JpaRepository<Bid, Long> {
}
