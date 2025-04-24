package org.example.auctionmaerketrealtime.domain.auction.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.auctionmaerketrealtime.domain.auction.dto.WebSocketAuctionCreateRequest;
import org.example.auctionmaerketrealtime.domain.auction.dto.WebSocketAuctionCreateResponse;
import org.example.auctionmaerketrealtime.domain.auction.service.AuctionService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/auction")
public class AuctionRoomController {

    private final StringRedisTemplate redisTemplate;
    private final AuctionService auctionService;

    // 경매방 생성
    @PostMapping("/start")
    public ResponseEntity<WebSocketAuctionCreateResponse> startAuctionRoom(
            @RequestBody WebSocketAuctionCreateRequest request) {
        auctionService.createAuction(request);

        String roomUrl = "http://localhost:8081/auction.html?auctionId=" + request.getAuctionId();

        log.info("경매방 생성완료 : {}", roomUrl);

        WebSocketAuctionCreateResponse response = new WebSocketAuctionCreateResponse(roomUrl);
        return ResponseEntity.ok(response);
    }

    // 유저별 입장 html 생성
    @PostMapping("/join")
    public ResponseEntity<WebSocketAuctionCreateResponse> joinAuctionRoom(@RequestBody Map<String, Object> request) {
        Object auctionIdObj = request.get("auctionId");
        Object nicknameObj = request.get("nickname");

        if (auctionIdObj == null || nicknameObj == null) {
            throw new IllegalArgumentException("경매 아이딩와 닉네임은 필수 입력입니다");
        }

        Long auctionId = Long.parseLong(auctionIdObj.toString());
        String nickname = nicknameObj.toString();

        String roomUrl = "http://localhost:8081/auction.html?auctionId=" + auctionId + "&nickname=" + nickname;
        log.info("참여용 경매 링크 생성: {}", roomUrl);

        return ResponseEntity.ok(new WebSocketAuctionCreateResponse(roomUrl));
    }
}
