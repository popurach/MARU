package com.bird.maru.auth.service;

import com.bird.maru.auth.service.dto.CustomUserDetails;
import com.bird.maru.common.redis.RedisCacheKey;
import com.bird.maru.common.util.JwtUtil;
import com.bird.maru.spot.repository.query.SpotCustomQueryRepository;
import java.time.Duration;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("authService")
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtUtil jwtUtil;
    private final SpotCustomQueryRepository spotCustomQueryRepository;

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

    @Override
    @Transactional(readOnly = true)
    public boolean authorizeToAuction(Long memberId, Long landmarkId) {
        return spotCustomQueryRepository.existsSpotByMemberAndLandmark(memberId, landmarkId);
    }

    private boolean isDenied(CustomUserDetails member) {
        return this.redisTemplate.opsForValue()
                                 .get(createRedisKey(member.getId())) == null;
    }

    private String createRedisKey(Long memberId) {
        return RedisCacheKey.REFRESH_TOKEN.getKey(memberId);
    }

}
