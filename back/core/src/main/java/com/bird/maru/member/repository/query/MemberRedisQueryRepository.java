package com.bird.maru.member.repository.query;

import com.bird.maru.common.redis.RedisCacheKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MemberRedisQueryRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    public Set<Long> findVisitedLandmarks(Long memberId) {
        Set<Object> members = redisTemplate.opsForSet().members(
                RedisCacheKey.createKey(RedisCacheKey.MEMBER_VISITED, memberId)
        );
        return members == null || members.isEmpty() ? new HashSet<>()
                : members.stream().map(m -> Long.parseLong(m.toString()))
                         .collect(Collectors.toSet());
    }

}
