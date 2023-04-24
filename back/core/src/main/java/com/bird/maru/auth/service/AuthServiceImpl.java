package com.bird.maru.auth.service;

import com.bird.maru.common.util.JwtUtil;
import com.bird.maru.domain.model.type.CustomUserDetails;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    public static final String REFRESH_TOKEN_PREFIX = "member_refresh:";

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtUtil jwtUtil;

    @Override
    public Map<String, String> generateToken(CustomUserDetails member) {
        String accessToken = jwtUtil.generateAccessToken(member);
        String refreshToken = jwtUtil.generateRefreshToken(member);

        redisTemplate.opsForValue()
                     .set(
                             createRedisKey(refreshToken),
                             refreshToken,
                             jwtUtil.getRefreshTokenExpirationTime()
                     );

        return Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken
        );
    }

    private String createRedisKey(String refreshToken) {
        return REFRESH_TOKEN_PREFIX + refreshToken;
    }

}
