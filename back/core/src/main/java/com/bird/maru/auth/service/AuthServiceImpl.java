package com.bird.maru.auth.service;

import com.bird.maru.common.redis.RedisCacheKey;
import com.bird.maru.common.util.JwtUtil;
import com.bird.maru.auth.service.dto.CustomUserDetails;
import java.time.Duration;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtUtil jwtUtil;

    @Override
    public Map<String, String> generateToken(CustomUserDetails member) {
        String accessToken = jwtUtil.generateAccessToken(member);
        String refreshToken = jwtUtil.generateRefreshToken(member);

        redisTemplate.opsForValue()
                     .set(
                             createRedisKey(member.getId()),
                             refreshToken,
                             Duration.ofMillis(jwtUtil.getRefreshTokenExpirationTime())
                     );

        return Map.of(
                "Access-Token", accessToken,
                "Refresh-Token", refreshToken
        );
    }

    @Override
    public String regenerateAccessToken(CustomUserDetails member) throws AccessDeniedException {
        if (isDenied(member)) {
            throw new AccessDeniedException("이 Refresh Token은 탈취 가능성이 있습니다.");
        }

        return jwtUtil.generateAccessToken(member);
    }

    @Override
    public void reportRefreshToken(CustomUserDetails member) {
        this.redisTemplate.delete(createRedisKey(member.getId()));
    }

    private boolean isDenied(CustomUserDetails member) {
        String refreshToken = this.redisTemplate.opsForValue()
                                                .get(createRedisKey(member.getId()));
        return refreshToken == null;
    }

    private String createRedisKey(Long memberId) {
        return RedisCacheKey.createKey(
                RedisCacheKey.REFRESH_TOKEN_KEY, memberId
        );
    }

}
