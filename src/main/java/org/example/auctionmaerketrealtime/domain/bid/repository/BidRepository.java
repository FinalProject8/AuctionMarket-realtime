package org.example.auctionmaerketrealtime.domain.bid.repository;

import org.example.auctionmaerketrealtime.domain.bid.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BidRepository extends JpaRepository<Bid, Long> {
    Optional<Bid> findTopByAuctionIdOrderByIdDesc(Long auctionId);
}
