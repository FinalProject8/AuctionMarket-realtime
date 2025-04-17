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
    private final AuctionRepository auctionRepository;
    private final BidRepository bidRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public void endAuction(Long auctionId) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new RuntimeException("Auction not found"));

        if (auction.getStatus() == AuctionStatus.ENDED) {
            log.info("경매 {}는 이미 종료된 상태입니다", auctionId);
            return;
        }
        auction.end();
        auctionRepository.save(auction);

        Optional<Bid> bidUser = bidRepository.findTopByAuctionIdOrderByIdDesc(auctionId);

        if (bidUser.isPresent()) {
            Bid winner = bidUser.get();
            log.info("경매 {} 종료 ! 낙찰자 : {}, 금액: {}", auctionId, winner.getUsername(), winner.getAmount());
            messagingTemplate.convertAndSendToUser(
                    winner.getUsername(),
                    "/queue/bid-result/",
                    new AuctionEndMessage(auctionId, winner.getAmount(), "낙찰 되셨습니다! 결제를 진행해주세요"));
        } else {
            log.info("경매 {} 종료! 입찰자가 없어 낙찰자가 존재하지 않습니다", auctionId);
        }
    }
}
