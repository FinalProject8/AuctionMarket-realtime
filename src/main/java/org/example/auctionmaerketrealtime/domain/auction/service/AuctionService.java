package org.example.auctionmaerketrealtime.domain.auction.service;

import lombok.RequiredArgsConstructor;
import org.example.auctionmaerketrealtime.common.general.AuctionUrlGeneral;
import org.example.auctionmaerketrealtime.domain.auction.dto.WebSocketAuctionCreateRequest;
import org.example.auctionmaerketrealtime.domain.auction.dto.WebSocketAuctionCreateResponse;
import org.example.auctionmaerketrealtime.domain.auction.entity.Auction;
import org.example.auctionmaerketrealtime.domain.auction.enums.AuctionStatus;
import org.example.auctionmaerketrealtime.domain.auction.exception.AuctionErrorCode;
import org.example.auctionmaerketrealtime.domain.auction.exception.AuctionException;
import org.example.auctionmaerketrealtime.domain.auction.repository.AuctionRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuctionService {
    private final AuctionRepository auctionRepository;
    private final AuctionUrlGeneral auctionUrlGeneral;
    private final StringRedisTemplate stringRedisTemplate;

    // 경매방 생성
    public WebSocketAuctionCreateResponse createAuction(WebSocketAuctionCreateRequest request) {
        validateAuction(request.getAuctionId());

        Auction auction = saveAuction(request);
        setAuctionEndTimeTTL(auction.getId(), request.getEndTime());

        String roomUrl = auctionUrlGeneral.generateRoomUrl(auction.getId());
        return new WebSocketAuctionCreateResponse(roomUrl);
    }

    // 유저별 입장 링크 생성
    public WebSocketAuctionCreateResponse generateJoinUrl(WebSocketAuctionCreateRequest request) {
        String joinUrl = auctionUrlGeneral.generateJoinUrl(
                request.getAuctionId(),
                request.getConsumerId(),
                request.getNickName()
        );
        return new WebSocketAuctionCreateResponse(joinUrl);
    }

    // 경매 유효 체크
    private void validateAuction(Long auctionId) {
        if (auctionRepository.existsById(auctionId)) {
            throw new AuctionException(AuctionErrorCode.AUCTION_ALREADY_EXISTS);
        }
    }

    // 경매 생성
    private Auction saveAuction(WebSocketAuctionCreateRequest request) {
        Auction auction = Auction.builder()
                .id(request.getAuctionId())
                .title(request.getProductName())
                .topPrice(request.getMinPrice())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .status(AuctionStatus.PROGRESS)
                .build();
        return auctionRepository.save(auction);
    }

    // 경매 종료 TTL 지정
    private void setAuctionEndTimeTTL(Long auctionId, LocalDateTime endTime) {
        long minutes = Duration.between(LocalDateTime.now(), endTime).toMinutes();
        if (minutes <= 0) minutes = 5;

        stringRedisTemplate.opsForValue().set(
                "auction:end:" + auctionId,
                "end",
                Duration.ofMinutes(minutes)
        );
    }
}
