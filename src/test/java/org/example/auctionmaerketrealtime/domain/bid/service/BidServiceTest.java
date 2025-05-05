package org.example.auctionmaerketrealtime.domain.bid.service;

import org.example.auctionmaerketrealtime.common.dto.BidMessage;
import org.example.auctionmaerketrealtime.common.handler.AuctionRedisHandler;
import org.example.auctionmaerketrealtime.domain.auction.entity.Auction;
import org.example.auctionmaerketrealtime.domain.auction.repository.AuctionRepository;
import org.example.auctionmaerketrealtime.domain.bid.repository.BidRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BidServiceTest {

    @Mock
    private BidRepository bidRepository;

    @Mock
    private AuctionRepository auctionRepository;

    @Mock
    private AuctionRedisHandler auctionRedisHandler;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @InjectMocks
    private BidService bidService;

    @Test
    void 경매를_찾을_수_없다() {
        Long auctionId = 1L;
        BidMessage bidMessage = new BidMessage(2L, 2L, "tester", 10000L, "BID");

        Mockito.when(auctionRepository.findById(auctionId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, ()-> bidService.placeBid(auctionId, bidMessage));
    }

    @Test
    void 경매가_시작하지않아_입찰을_할_수_없다() {
        Long auctionId = 1L;
        BidMessage bidMessage = new BidMessage(1L, 2L, "tester", 10000L, "BID");
        Auction auction = Auction.builder()
                .startTime(LocalDateTime.now().plusMinutes(5))
                .endTime(LocalDateTime.now().plusMinutes(10))
                .topPrice(9000L)
                .build();
        Mockito.when(auctionRepository.findById(auctionId)).thenReturn(Optional.of(auction));

        assertThrows(RuntimeException.class, ()-> bidService.placeBid(auctionId, bidMessage));
    }

    @Test
    void 경매가_종료되어_입찰을_할_수_없다() {
        Long auctionId = 1L;
        BidMessage bidMessage = new BidMessage(1L, 2L, "tester", 10000L, "BID");
        Auction auction = Auction.builder()
                .startTime(LocalDateTime.now().minusMinutes(5))
                .endTime(LocalDateTime.now().minusMinutes(10))
                .topPrice(9000L)
                .build();
        Mockito.when(auctionRepository.findById(auctionId)).thenReturn(Optional.of(auction));

        assertThrows(RuntimeException.class, ()-> bidService.placeBid(auctionId, bidMessage));
    }

    @Test
    void 최고입찰가보다_입찰가가_낮아_입찰을_할_수_없다() {
        Long auctionId = 1L;
        BidMessage bidMessage = new BidMessage(1L, 2L, "tester", 8000L, "BID");
        Auction auction = Auction.builder()
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusMinutes(10))
                .topPrice(9000L)
                .build();
        Mockito.when(auctionRepository.findById(auctionId)).thenReturn(Optional.of(auction));

        assertThrows(RuntimeException.class, ()-> bidService.placeBid(auctionId, bidMessage));
    }

    @Test
    void 입찰을_할_수_있다() {
        Long auctionId = 1L;
        BidMessage bidMessage = new BidMessage(1L, 2L, "tester", 10000L, "BID");
        Auction auction = Auction.builder()
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusMinutes(10))
                .topPrice(9000L)
                .build();

        Mockito.when(auctionRepository.findById(auctionId)).thenReturn(Optional.of(auction));
        Mockito.when(bidRepository.save(Mockito.any())).thenAnswer(invocation -> invocation.getArgument(0));
        Mockito.when(auctionRepository.save(Mockito.any())).thenAnswer(invocation -> invocation.getArgument(0));
        Mockito.doNothing().when(auctionRedisHandler).saveTopBid(Mockito.anyLong(),Mockito.anyString(), Mockito.anyLong(),Mockito.anyLong(),Mockito.any());
        Mockito.when(redisTemplate.convertAndSend(anyString(), any())).thenReturn(1L);


        BidMessage result = bidService.placeBid(auctionId, bidMessage);

        assertEquals(bidMessage, result);
    }
}