package org.example.auctionmaerketrealtime.domain.bid.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.auctionmaerketrealtime.common.handler.AuctionRedisHandler;
import org.example.auctionmaerketrealtime.domain.auction.entity.Auction;
import org.example.auctionmaerketrealtime.domain.auction.repository.AuctionRepository;
import org.example.auctionmaerketrealtime.domain.bid.entity.Bid;
import org.example.auctionmaerketrealtime.domain.bid.repository.BidRepository;
import org.example.auctionmaerketrealtime.common.dto.BidMessage;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class BidService {
    private final BidRepository bidRepository;
    private final AuctionRepository auctionRepository;
    private final AuctionRedisHandler auctionRedisHandler;
    private final RedisTemplate<String, Object> redisTemplate;

    @Transactional
    public BidMessage placeBid(
            Long auctionId,
            BidMessage bidMessage) {
        log.info("Place bid for auctionId {}, username {}, amount {}", auctionId, bidMessage.getUsername(), bidMessage.getAmount());
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new RuntimeException("경매를 찾을 수 없습니다"));

        LocalDateTime now = LocalDateTime.now();

        if (auction.getStartTime().isAfter(now)) {
            log.warn("경매 시작전: 입찰 거부");
            throw new RuntimeException("경매가 아직 시작하지 않았습니다");
        }

        if (auction.getEndTime().isBefore(now)) {
            log.warn("경매 종료됨 : 입찰거부");
            throw new RuntimeException("경매가 이미 종료되었습니다");
        }

        Long currentTopPrice = auction.getTopPrice();
        Long newBidAmount = bidMessage.getAmount();

        log.info("📌 현재 최고가: {}, 들어온 입찰가: {}", currentTopPrice, newBidAmount);

        if (bidMessage.getAmount() <= auction.getTopPrice()) {
            log.warn("❌ 입찰가가 최고가 이하입니다. 저장하지 않음");
            throw new RuntimeException("현재 최고가보다 낮아 입찰할 수 없습니다");
        }

        // 입찰 저장
        Bid bid = Bid.builder()
                .consumerId(bidMessage.getConsumerId())
                .username(bidMessage.getUsername())
                .amount(bidMessage.getAmount())
                .auction(auction)
                .createdAt(LocalDateTime.now())
                .build();
        bidRepository.save(bid);

        auction.setTopPrice(newBidAmount);
        auctionRepository.save(auction);

        auctionRedisHandler.saveTopBid(auctionId, bidMessage.getUsername(), bidMessage.getAmount(), auction.getEndTime());

        log.info("✅ 입찰 저장 완료! DB에 반영");
        redisTemplate.convertAndSend("auction:bid:" + auctionId, bidMessage);
        log.info("서버간 입찰가 전송 완료");
        return bidMessage;
    }
}
