package org.example.auctionmaerketrealtime.domain.auction.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class WebSocketAuctionCreateResponse {
    private String websocketUrl;

    public WebSocketAuctionCreateResponse(String websocketUrl) {
        this.websocketUrl = websocketUrl;
    }

    public String getWebsocketUrl() {
        return websocketUrl;
    }

    public void setWebsocketUrl(String websocketUrl) {
        this.websocketUrl = websocketUrl;
    }
}
