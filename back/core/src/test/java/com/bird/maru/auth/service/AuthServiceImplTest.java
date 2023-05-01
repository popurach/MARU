package com.bird.maru.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.bird.maru.auth.service.dto.CustomUserDetails;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.AccessDeniedException;

@SpringBootTest
class AuthServiceImplTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    @DisplayName("토큰 생성 및 저장 테스트")
    void generateToken() {
        //given
        CustomUserDetails testMember = getTestMember(1L);

        // when
        Map<String, String> tokens = authService.generateToken(testMember);

        // then
        assertThat(tokens.get("Access-Token")).isNotEmpty();
        assertThat(tokens.get("Refresh-Token")).isNotEmpty();
        assertThat(
                redisTemplate.opsForValue()
                             .get(createRedisKey(1L))
        ).isEqualTo(tokens.get("Refresh-Token"));
    }

    @Test
    @DisplayName("Access Token 재발급 테스트")
    void regenerateAccessToken() {
        // given
        CustomUserDetails testMember = getTestMember(1L);

        // when
        authService.generateToken(testMember);

        // then
        assertThat(authService.regenerateAccessToken(testMember)).isNotEmpty();
    }

    @Test
    @DisplayName("Refresh Token 신고(리폿) 테스트")
    void reportRefreshToken() {
        // given
        CustomUserDetails testMember = getTestMember(1L);

        // when
        authService.generateToken(testMember);
        authService.reportRefreshToken(testMember);

        // then
        assertThatThrownBy(
                () -> authService.regenerateAccessToken(testMember)
        ).isInstanceOf(AccessDeniedException.class);
    }

    private CustomUserDetails getTestMember(Long id) {
        return CustomUserDetails.builder()
                                .id(id)
                                .build();
    }

    private String createRedisKey(Long id) {
        return AuthServiceImpl.REFRESH_TOKEN_PREFIX + id;
    }

}