package org.example.auctionmaerketrealtime.common.redis.config;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuctionRedisService {
    private final RedisTemplate<String, String> redisTemplate;

    public void saveAuctionRoomWithTTL(Long auctionId, Duration ttl) {
        String key = "auction:room" + auctionId;
        redisTemplate.opsForValue().set(key,"ACTIVE", ttl);
    }

    public boolean isAuctionRoomActive(Long auctionId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey("auction:room" + auctionId));
    }

    public void saveTopBid(Long auctionId, String username, Long amount) {
        String key = "auction:top:" + auctionId;
//        Map<String, Object> data = new HashMap<>();
//        data.put("amount", amount);
//        data.put("username", username);
//
//        redisTemplate.opsForHash().putAll(key, data);
        redisTemplate.opsForHash().put(key, "amount", String.valueOf(amount));
        redisTemplate.opsForHash().put(key, "username", username);

        System.out.println("redis 저장 키: " + key);
        System.out.println("username: " + username + " amount: " + amount);
    }

    public /*Map<Object, Object>*/List<Object> getTopBid(Long auctionId) {
        String key = "auction:top:" + auctionId;
        return redisTemplate.opsForHash().values(key).stream().toList();
    }
}
