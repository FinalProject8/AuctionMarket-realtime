package org.example.auctionmaerketrealtime.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(@Value("${MAIN.SERVER.URL}") String serverUrl) {
        return WebClient.builder()
                .baseUrl(serverUrl)
                .build();
    }
}
