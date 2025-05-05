package org.example.auctionmaerketrealtime.common.general;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AuctionUrlGeneral {

    private final String baseUrl;

    // 생성자를 통해 프로퍼티 값 주입
    public AuctionUrlGeneral(@Value("${app.auction.base-url}") String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String generateRoomUrl(Long auctionId) {
        // 주입받은 baseUrl 사용
        return baseUrl + "/auction.html?auctionId=" + auctionId; // 경로 수정됨
    }

    public String generateJoinUrl(Long auctionId, Long consumerId, String nickname) {
        // 주입받은 baseUrl 사용
        return baseUrl + "/auction.html?auctionId=" + auctionId + "&consumerId=" + consumerId + "&nickname=" + nickname; // 경로 수정됨
    }
}
