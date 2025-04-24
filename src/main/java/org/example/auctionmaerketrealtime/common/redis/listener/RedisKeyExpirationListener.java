//package org.example.auctionmaerketrealtime.common.redis.listener;
//
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.connection.Message;
//import org.springframework.data.redis.connection.MessageListener;
//import org.springframework.data.redis.listener.ChannelTopic;
//import org.springframework.data.redis.listener.RedisMessageListenerContainer;
//import org.springframework.stereotype.Service;
//import org.springframework.web.reactive.function.client.WebClient;
//
//@Service
//public class RedisKeyExpirationListener implements MessageListener {
//
//    @Autowired
//    private RedisMessageListenerContainer container;
//
//    private final WebClient webClient = WebClient.create("http://localhost:8081");
//
//    @PostConstruct
//    public void init() {
//        container.addMessageListener(this, new ChannelTopic("__keyevent@0__:expired"));
//    }
//
//    @Override
//    public void onMessage(Message message, byte[] pattern) {
//        String expiredKey = message.toString();
//        if (expiredKey.startsWith("auction:")) {
//            String auctionId = expiredKey.replace("auction:", "");
//
//            webClient.post()
//                    .uri("/api/auction/expired")
//                    .bodyValue(new AuctionExpireRequest(Long.parseLong(auctionId)))
//                    .retrieve()
//                    .bodyToMono(Void.class)
//                    .subscribe();
//        }
//    }
//
//    public record AuctionExpireRequest(Long auctionId) {}
//}
