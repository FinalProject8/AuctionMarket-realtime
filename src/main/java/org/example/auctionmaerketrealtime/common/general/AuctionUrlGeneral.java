package org.example.auctionmaerketrealtime.common.general;

import org.springframework.stereotype.Component;

@Component
public class AuctionUrlGeneral {

    private final String BASE_URL = "https://localhost:8081/auction/.html";

    public String generateRoomUrl(Long auctionId) {
        return BASE_URL + "?auctionId=" + auctionId;
    }

    public String generateJoinUrl(Long auctionId, Long consumerId, String nickname) {
        return BASE_URL + "?auctionId=" + auctionId + "&consumerId=" + consumerId + "&nickname=" + nickname;
    }
}
