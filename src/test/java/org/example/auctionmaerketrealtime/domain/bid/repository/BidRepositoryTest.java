package org.example.auctionmaerketrealtime.domain.bid.repository;

import org.example.auctionmaerketrealtime.domain.auction.entity.Auction;
import org.example.auctionmaerketrealtime.domain.auction.repository.AuctionRepository;
import org.example.auctionmaerketrealtime.domain.bid.entity.Bid;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@DataJpaTest
class BidRepositoryTest {

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private AuctionRepository auctionRepository;

    @Test
    void 최고날착자의_아이디를_조회할_수_있다() {
        // given
        Auction auction = Auction.builder()
                .id(10L)
                .title("Test")
                .build();
        auctionRepository.save(auction);

        Bid bid1 = Bid.builder()
                .consumerId(1L)
                .auction(auction)
                .amount(10000L)
                .createdAt(LocalDateTime.now().minusSeconds(2))
                .build();

        Bid bid2 = Bid.builder()
                .consumerId(2L)
                .auction(auction)
                .amount(11000L)
                .createdAt(LocalDateTime.now().minusSeconds(1))
                .build();

        Bid bid3 = Bid.builder()
                .consumerId(3L)
                .auction(auction)
                .amount(12000L)
                .createdAt(LocalDateTime.now())
                .build();

        bidRepository.saveAll(List.of(bid1, bid2, bid3));
        // when
        Optional<Bid> lastBid = bidRepository.findTopByAuctionIdOrderByIdDesc(auction.getId());
        // then
        assertThat(lastBid).isPresent();
        assertThat(lastBid.get().getConsumerId()).isEqualTo(3L);
    }

}