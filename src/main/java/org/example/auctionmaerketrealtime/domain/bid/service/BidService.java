package org.example.auctionmaerketrealtime.domain.bid.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.auctionmaerketrealtime.domain.auction.entity.Auction;
import org.example.auctionmaerketrealtime.domain.auction.repository.AuctionRepository;
import org.example.auctionmaerketrealtime.domain.bid.entity.Bid;
import org.example.auctionmaerketrealtime.domain.bid.repository.BidRepository;
import org.example.auctionmaerketrealtime.websocket.BidMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class BidService {
    private final BidRepository bidRepository;
    private final AuctionRepository auctionRepository;

    private final Duration enterLimit = Duration.ofMinutes(10);

    @Transactional
    public BidMessage placeBid(
            Long auctionId,
            BidMessage bidMessage) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new RuntimeException("Auction not found"));

        Long currentTopPrice = auction.getTopPrice();
        Long newBidAmount = bidMessage.getAmount();

        log.info("📌 현재 최고가: {}, 들어온 입찰가: {}", currentTopPrice, newBidAmount);

        if (bidMessage.getAmount() <= auction.getTopPrice()) {
            log.warn("❌ 입찰가가 최고가 이하입니다. 저장하지 않음");
            throw new RuntimeException("Top price exceeded");
        }

        // 입찰 저장
        Bid bid = Bid.builder()
                .username(bidMessage.getUsername())
                .amount(bidMessage.getAmount())
                .auction(auction)
                .createdAt(LocalDateTime.now())
                .build();
        bidRepository.save(bid);

        auction.setTopPrice(newBidAmount);
        auctionRepository.save(auction);

        log.info("✅ 입찰 저장 완료! DB에 반영");
        return bidMessage;
    }
}
