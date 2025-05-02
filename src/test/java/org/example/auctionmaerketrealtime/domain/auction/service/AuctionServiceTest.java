package org.example.auctionmaerketrealtime.domain.auction.service;

import org.example.auctionmaerketrealtime.common.general.AuctionUrlGeneral;
import org.example.auctionmaerketrealtime.domain.auction.dto.WebSocketAuctionCreateRequest;
import org.example.auctionmaerketrealtime.domain.auction.dto.WebSocketAuctionCreateResponse;
import org.example.auctionmaerketrealtime.domain.auction.repository.AuctionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class AuctionServiceTest {

    @Mock
    private AuctionRepository auctionRepository;

    @Mock
    private AuctionUrlGeneral auctionUrlGeneral;

    @Mock
    private StringRedisTemplate stringRedisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private AuctionService auctionService;

    @Test
    void 경매를_정상적으로_생성한다() {
        // given
        WebSocketAuctionCreateRequest request = new WebSocketAuctionCreateRequest();
        ReflectionTestUtils.setField(request, "auctionId", 1L);
        ReflectionTestUtils.setField(request, "productName", "테스트 상품");
        ReflectionTestUtils.setField(request, "minPrice", 10000L);
        ReflectionTestUtils.setField(request, "startTime", LocalDateTime.now());
        ReflectionTestUtils.setField(request, "endTime", LocalDateTime.now().plusMinutes(10));

        Mockito.when(auctionRepository.existsById(1L)).thenReturn(false);
        Mockito.when(auctionRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        Mockito.when(auctionUrlGeneral.generateRoomUrl(1L)).thenReturn("http://localhost:8081/auction.html?auctionId=1");
        Mockito.when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
        // when
        WebSocketAuctionCreateResponse response = auctionService.createAuction(request);
        // then
        assertThat(response.getWebsocketUrl()).isEqualTo("http://localhost:8081/auction.html?auctionId=1");

        verify(auctionRepository).save(any());
        verify(valueOperations).set(eq("auction:end:1"), eq("end"), any());
    }

    @Test
    void 이미_존재하는_경매ID로_경매_생성에_실패한다() {
        // given
        WebSocketAuctionCreateRequest request = new WebSocketAuctionCreateRequest();
        ReflectionTestUtils.setField(request, "auctionId", 1L);
        ReflectionTestUtils.setField(request, "productName", "테스트 상품");
        ReflectionTestUtils.setField(request, "minPrice", 10000L);
        ReflectionTestUtils.setField(request, "startTime", LocalDateTime.now());
        ReflectionTestUtils.setField(request, "endTime", LocalDateTime.now().plusMinutes(10));

        Mockito.when(auctionRepository.existsById(1L)).thenReturn(true);
        // when & then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> auctionService.createAuction(request));

        assertThat(exception.getMessage()).isEqualTo("이미 존재하는 경매 ID입니다: 1");

        verify(auctionRepository, never()).save(any());
    }

    @Test
    void 유저가_입장을_요청해_링크를_전달한다() {
        // givne
        WebSocketAuctionCreateRequest request = new WebSocketAuctionCreateRequest();
        ReflectionTestUtils.setField(request, "auctionId", 1L);
        ReflectionTestUtils.setField(request, "consumerId", 2L);
        ReflectionTestUtils.setField(request, "nickName", "tester");

        String expectedUrl = "http://localhost:8081/auction.html?auctionId=1&nickName=tester";
        Mockito.when(auctionUrlGeneral.generateJoinUrl(1L, 2L, "tester")).thenReturn(expectedUrl);
        // when
        WebSocketAuctionCreateResponse response = auctionService.generateJoinUrl(request);

        // then
        assertThat(response.getWebsocketUrl()).isEqualTo(expectedUrl);
    }

}