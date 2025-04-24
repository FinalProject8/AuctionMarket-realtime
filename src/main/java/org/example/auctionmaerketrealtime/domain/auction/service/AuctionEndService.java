package org.example.auctionmaerketrealtime.domain.auction.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.auctionmaerketrealtime.domain.auction.AuctionEndMessage;
import org.example.auctionmaerketrealtime.domain.auction.entity.Auction;
import org.example.auctionmaerketrealtime.domain.auction.enums.AuctionStatus;
import org.example.auctionmaerketrealtime.domain.auction.repository.AuctionRepository;
import org.example.auctionmaerketrealtime.domain.bid.entity.Bid;
import org.example.auctionmaerketrealtime.domain.bid.repository.BidRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuctionEndService {

    private final BidRepository bidRepository;

    public AuctionEndMessage getWinnerInfo(Long auctionId) {
        log.info(" 낙찰자 조회 서비스 진입 auction {}", auctionId);
        Bid winner = bidRepository.findTopByAuctionIdOrderByIdDesc(auctionId)
                .orElse(null);

        if (winner == null) {
            log.info("입찰이 존재하지 않아 낙찰자가 없습니다");
            return new AuctionEndMessage(auctionId, null, null,"입찰이 존재하지 않아 낙찰자가 없습니다");
        }
        log.info("낙찰자 정보 auctionId: {}, winner: {}", auctionId, winner);
        return new AuctionEndMessage(auctionId, winner.getUsername(), winner.getAmount(), "낙찰 되셨습니다! 결제를 진행해주세요");
    }
}
