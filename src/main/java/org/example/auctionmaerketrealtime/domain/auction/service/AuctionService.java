package org.example.auctionmaerketrealtime.domain.auction.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.auctionmaerketrealtime.domain.auction.dto.WebSocketAuctionCreateRequest;
import org.example.auctionmaerketrealtime.domain.auction.entity.Auction;
import org.example.auctionmaerketrealtime.domain.auction.enums.AuctionStatus;
import org.example.auctionmaerketrealtime.domain.auction.repository.AuctionRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuctionService {
    private final AuctionRepository auctionRepository;
    private final StringRedisTemplate stringRedisTemplate;

    // 경매방 종료시간 Redis TTL 로 설정
    public void createAuction(WebSocketAuctionCreateRequest request) {
        Long auctionId = request.getAuctionId();

        if (auctionRepository.existsById(auctionId)) {
            log.error("{} 경매가 이미 존재합니다 ", auctionId);
            return;
        }

        Auction auction = Auction.builder()
                .id(auctionId)
                .title(request.getProductName())
                .topPrice(request.getMinPrice())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .status(AuctionStatus.PROGRESS)
                .build();

        auctionRepository.save(auction);

        long endDelay = Duration.between(LocalDateTime.now(), request.getEndTime()).toMinutes();

        if (endDelay <= 0) endDelay = 5;

        stringRedisTemplate.opsForValue().set(
                "auction:end:" + auction.getId(),
                "end", Duration.ofMinutes(endDelay));

        log.info("Redis TTL 등록: endTime {}분", endDelay);
    }
}
