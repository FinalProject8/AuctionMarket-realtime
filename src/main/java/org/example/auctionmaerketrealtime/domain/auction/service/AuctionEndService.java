package org.example.auctionmaerketrealtime.domain.auction.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.auctionmaerketrealtime.domain.auction.dto.AuctionEndMessage;
import org.example.auctionmaerketrealtime.domain.bid.entity.Bid;
import org.example.auctionmaerketrealtime.domain.bid.repository.BidRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuctionEndService {

    private final BidRepository bidRepository;
    private final WebClient webClient;

    // 메인 서버로 직접 보내는 메소드
    public void sendWinner(Long auctionId) {
        AuctionEndMessage winnerInfo = getWinnerInfo(auctionId);
        sendWinnerToMain(winnerInfo);
    }

    // 낙찰자 정보 조회
    public AuctionEndMessage getWinnerInfo(Long auctionId) {
        log.info(" 낙찰자 조회 서비스 진입 auction {}", auctionId);
        Bid winner = bidRepository.findTopByAuctionIdOrderByIdDesc(auctionId)
                .orElse(null);

        if (winner == null) {
            log.info("입찰이 존재하지 않아 낙찰자가 없습니다");
            return new AuctionEndMessage(auctionId, null, null);
        }
        log.info("낙찰자 정보 auctionId: {}, winner: {}", auctionId, winner);

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
                .bodyToMono(AuctionEndMessage.class)
                .subscribe(
                        success -> log.info("메인 서버로 낙찰자 정보 전송 성공"),
                        error -> log.error("메인 서버로 낙찰자 정보 전송 실패", error)
                );
    }
}
