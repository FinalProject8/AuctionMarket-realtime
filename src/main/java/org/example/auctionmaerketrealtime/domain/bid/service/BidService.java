package org.example.auctionmaerketrealtime.domain.bid.service;

import lombok.RequiredArgsConstructor;
import org.example.auctionmaerketrealtime.common.handler.AuctionRedisHandler;
import org.example.auctionmaerketrealtime.domain.auction.entity.Auction;
import org.example.auctionmaerketrealtime.domain.auction.exception.AuctionErrorCode;
import org.example.auctionmaerketrealtime.domain.auction.exception.AuctionException;
import org.example.auctionmaerketrealtime.domain.auction.repository.AuctionRepository;
import org.example.auctionmaerketrealtime.domain.bid.entity.Bid;
import org.example.auctionmaerketrealtime.domain.bid.exception.BidErrorCode;
import org.example.auctionmaerketrealtime.domain.bid.exception.BidException;
import org.example.auctionmaerketrealtime.domain.bid.repository.BidRepository;
import org.example.auctionmaerketrealtime.common.dto.BidMessage;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BidService {
    private final BidRepository bidRepository;
    private final AuctionRepository auctionRepository;
    private final AuctionRedisHandler auctionRedisHandler;
    private final RedisTemplate<String, Object> redisTemplate;

    @Transactional
    public BidMessage placeBid(Long auctionId, BidMessage bidMessage) {
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(
                () -> new AuctionException(AuctionErrorCode.AUCTION_NOT_FOUND));

        LocalDateTime now = LocalDateTime.now();

        if (auction.getStartTime().isAfter(now)) {
            throw new AuctionException(AuctionErrorCode.AUCTION_NOT_STARTED);
        }

        if (auction.getEndTime().isBefore(now)) {
            throw new AuctionException(AuctionErrorCode.AUCTION_IS_ENDED);
        }

        Long newBidAmount = bidMessage.getAmount();

        if (bidMessage.getAmount() <= auction.getTopPrice()) {
            throw new BidException(BidErrorCode.LOWER_THAN_CURRENT_BID);
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

        auction.updateTopPrice(newBidAmount);
        auctionRepository.save(auction);

        auctionRedisHandler.saveTopBid(auctionId, bidMessage.getUsername(), bidMessage.getConsumerId(), bidMessage.getAmount(), auction.getEndTime());

        redisTemplate.convertAndSend("auction:bid:" + auctionId, bidMessage);

        return bidMessage;
    }

    public BidMessage buildEnterMessage(BidMessage message) {
        return BidMessage.builder()
                .auctionId(message.getAuctionId())
                .username(message.getUsername())
                .amount(message.getAmount())
                .consumerId(message.getConsumerId())
                .type("ENTER")
                .build();
    }

    public BidMessage buildBidResponse(BidMessage saved) {
        return BidMessage.builder()
                .auctionId(saved.getAuctionId())
                .username(saved.getUsername())
                .amount(saved.getAmount())
                .consumerId(saved.getConsumerId())
                .type("BID")
                .build();
    }
}
