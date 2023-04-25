package com.bird.maru.auth.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.bird.maru.domain.model.type.CustomUserDetails;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

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
        CustomUserDetails testMember = CustomUserDetails.builder().id(1L).build();

        // when
        Map<String, String> tokens = authService.generateToken(testMember);

        // then
        assertThat(tokens.get("Access-Token")).isNotEmpty();
        assertThat(tokens.get("Refresh-Token")).isNotEmpty();
        assertThat(
                redisTemplate.opsForValue()
                             .get(AuthServiceImpl.REFRESH_TOKEN_PREFIX + 1L)
        ).isEqualTo(tokens.get("Refresh-Token"));
    }

    @Test
    void regenerateAccessToken() {
    }

    @Test
    void reportRefreshToken() {
    }

}