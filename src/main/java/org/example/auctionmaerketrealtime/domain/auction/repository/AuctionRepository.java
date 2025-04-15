package org.example.auctionmaerketrealtime.domain.auction.repository;

import org.example.auctionmaerketrealtime.domain.auction.entity.Auction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionRepository extends JpaRepository<Auction, Long> {
}
