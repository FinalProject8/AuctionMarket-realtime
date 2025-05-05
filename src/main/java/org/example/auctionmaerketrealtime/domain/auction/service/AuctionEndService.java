package org.example.auctionmaerketrealtime.domain.auction.service;

import lombok.RequiredArgsConstructor;
import org.example.auctionmaerketrealtime.domain.auction.dto.AuctionEndMessage;
import org.example.auctionmaerketrealtime.domain.auction.entity.Auction;
import org.example.auctionmaerketrealtime.domain.auction.repository.AuctionRepository;
import org.example.auctionmaerketrealtime.domain.bid.entity.Bid;
import org.example.auctionmaerketrealtime.domain.bid.repository.BidRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class AuctionEndService {

    private final AuctionRepository auctionRepository;
    private final BidRepository bidRepository;
    private final WebClient webClient;

    // 메인 서버로 직접 보내는 메소드
    public void sendWinner(Long auctionId) {
        AuctionEndMessage winnerInfo = getWinnerInfo(auctionId);
        sendWinnerToMain(winnerInfo);
    }

    // 낙찰자 정보 조회
    public AuctionEndMessage getWinnerInfo(Long auctionId) {
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(
                () -> new IllegalArgumentException("해당 경매는 존재하지 않습니다"));

        Bid winner = bidRepository.findTopByAuctionIdOrderByIdDesc(auctionId)
                .orElseThrow(() -> new IllegalArgumentException("입찰이 존재하지 않아 낙찰자가 없습니다"));

        auction.end();

        return new AuctionEndMessage(
                auctionId,
                winner.getConsumerId(),
                winner.getAmount());
    }

    // 실제로 메인서버로 정보를 보내는 로직
    private void sendWinnerToMain(AuctionEndMessage auctionEndMessage) {
        webClient.post()
                .uri("/v1/auctions/end")
                .bodyValue(auctionEndMessage)
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe();
    }
}
