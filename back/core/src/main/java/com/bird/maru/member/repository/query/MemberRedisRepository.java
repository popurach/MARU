package com.bird.maru.member.repository.query;

import com.bird.maru.common.redis.RedisCacheKey;
import com.bird.maru.common.util.TimeUtil;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MemberRedisRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    public Set<Long> findVisitedLandmarks(Long memberId) {
        Set<Object> members = redisTemplate.opsForSet().members(
                RedisCacheKey.MEMBER_VISITED.getKey(memberId)
        );
        return members == null || members.isEmpty() ? new HashSet<>()
                : members.stream().map(m -> Long.parseLong(m.toString()))
                         .collect(Collectors.toSet());
    }

    public Boolean existVisitedLandmark(Long memberId, Long landmarkId) {
        return redisTemplate.opsForSet().isMember(
                RedisCacheKey.MEMBER_VISITED.getKey(memberId), landmarkId.toString()
        );
    }

    public Long insertVisitLandmark(Long memberId, Long landmarkId) {
        SetOperations<String, Object> ops = redisTemplate.opsForSet();
        LocalDateTime midnight = TimeUtil.getMidnightDate();
        long expirationInSecs = midnight.toEpochSecond(ZoneOffset.UTC) - LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        String key = RedisCacheKey.MEMBER_VISITED.getKey(memberId);
        Long result = ops.add(key, landmarkId.toString());
        redisTemplate.expire(key, expirationInSecs, TimeUnit.SECONDS);
        return result;
    }

}
