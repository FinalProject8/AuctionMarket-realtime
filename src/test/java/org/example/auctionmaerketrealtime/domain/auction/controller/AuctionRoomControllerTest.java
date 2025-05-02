package org.example.auctionmaerketrealtime.domain.auction.controller;

import org.example.auctionmaerketrealtime.domain.auction.dto.WebSocketAuctionCreateRequest;
import org.example.auctionmaerketrealtime.domain.auction.dto.WebSocketAuctionCreateResponse;
import org.example.auctionmaerketrealtime.domain.auction.service.AuctionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AuctionRoomController.class)
class AuctionRoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuctionService auctionService;

    @Test
    void 경매방을_생성() throws Exception {
        String uri = "http://localhost:8081/auction.html?auctionId=1";
        WebSocketAuctionCreateResponse response = new WebSocketAuctionCreateResponse(uri);

        Mockito.when(auctionService.createAuction(Mockito.any())).thenReturn(response);

        String json = """
                {
                "auctionId": 1,
                "consumerId": 2,
                "nickname": "tester",
                "productName": "shoes",
                "minPrice": 10000,
                "startTime": "2025-05-01T14:00:00",
                "endTime": "2025-05-01T15:00:00"
                }
                """;
        mockMvc.perform(post("/internal/auction/start")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.websocketUrl").value(uri));
    }

    @Test
    void 유저별_입장_링크를_생성() throws Exception {
        String uri = "http://localhost:8081/auction.html?auctionId=1&nickName=tester";
        WebSocketAuctionCreateResponse response = new WebSocketAuctionCreateResponse(uri);

        Mockito.when(auctionService.generateJoinUrl(Mockito.any())).thenReturn(response);

        String json = """
                {
                "auctionId": 1,
                "consumerId": 2,
                "nickName": "tester"
                }
                """;
        mockMvc.perform(post("/internal/auction/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.websocketUrl").value(uri));

    }
}