package org.example.auctionmaerketrealtime.common.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuctionRedisHandler {
    private final RedisTemplate<String, String> redisTemplate;

    public void saveTopBid(Long auctionId, String username, Long amount, Long consumerId, LocalDateTime auctionEndTime) {
        String key = "auction:top:" + auctionId;

        redisTemplate.opsForHash().put(key, "amount", String.valueOf(amount));
        redisTemplate.opsForHash().put(key, "username", username);
        redisTemplate.opsForHash().put(key, "consumerId", String.valueOf(consumerId));

        Duration ttl = Duration.between(LocalDateTime.now(), auctionEndTime);

        if (ttl.isNegative() || ttl.isZero()) {
            ttl = Duration.ofMinutes(5);
        }
        redisTemplate.expire(key, ttl);

        System.out.println("redis 저장 키: " + key);
        System.out.println("username: " + username + " amount: " + amount + " consumerId: " + consumerId);
    }
}
