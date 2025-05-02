package org.example.auctionmaerketrealtime.domain.bid.controller;

import org.example.auctionmaerketrealtime.common.dto.BidMessage;
import org.example.auctionmaerketrealtime.domain.auction.entity.Auction;
import org.example.auctionmaerketrealtime.domain.auction.enums.AuctionStatus;
import org.example.auctionmaerketrealtime.domain.auction.repository.AuctionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuctionStompControllerTest {

    @Autowired
    private AuctionRepository auctionRepository;

    @LocalServerPort
    private int port;

    private StompSession session;

    @BeforeEach
    void setUp() throws Exception {
        WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSessionHandlerAdapter handlerAdapter = new StompSessionHandlerAdapter() {};
        session = stompClient
                .connect("ws://localhost:" + port + "/ws-stomp", handlerAdapter)
                .get(5, TimeUnit.SECONDS);
        while (!session.isConnected()) {
            Thread.sleep(100);
        }
        Auction auction = Auction.builder()
                .id(1L) // 이 ID가 테스트에서 사용하는 auctionId와 일치해야 함
                .title("테스트 경매")
                .startTime(LocalDateTime.now().minusMinutes(1))
                .endTime(LocalDateTime.now().plusMinutes(5))
                .topPrice(10000L)
                .status(AuctionStatus.PROGRESS) // enum 값 주의
                .build();

        auctionRepository.save(auction);
    }

    @AfterEach
    void tearDown() {
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
    }

    @Test
    void 유저가_경매_입장_메시지를_보내면_브로드캐스트된다() throws Exception {
        // given
        Long auctionId = 1L;
        String destination = "/pub/auction/" + auctionId + "/enter";
        String subsciption = "/topic/auction/" + auctionId;

        CompletableFuture<BidMessage> bidFuture = new CompletableFuture<>();
        session.subscribe(subsciption, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return BidMessage.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                bidFuture.complete((BidMessage) payload);
            }
        });

        BidMessage bidMessage = new BidMessage();
        bidMessage.setUsername("tester");

        // when
        session.send(destination, bidMessage);
        // then
        BidMessage received = bidFuture.get(5, TimeUnit.SECONDS);

        assertThat(received.getUsername()).isEqualTo("tester");
        assertThat(received.getType()).isEqualTo("ENTER");
    }

    @Test
    void 유저가_입찰을_한다() throws Exception {
        // given
        Long auctionId = 1L;
        String destination = "/pub/auction/" + auctionId + "/bid";
        String subsciption = "/topic/auction/" + auctionId;

        CompletableFuture<BidMessage> bidFuture = new CompletableFuture<>();
        session.subscribe(subsciption, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return BidMessage.class;
            }
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                bidFuture.complete((BidMessage) payload);
            }
        });

        BidMessage bidMessage = new BidMessage();
        bidMessage.setUsername("tester");
        bidMessage.setAmount(11000L);
        // when
        session.send(destination, bidMessage);
        // then
        BidMessage received = bidFuture.get(10, TimeUnit.SECONDS);
        assertThat(received.getUsername()).isEqualTo("tester");
        assertThat(received.getType()).isEqualTo("BID");
        assertThat(received.getAmount()).isEqualTo(11000L);
    }
}