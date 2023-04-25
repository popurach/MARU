package com.bird.maru.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.bird.maru.domain.model.type.CustomUserDetails;
import java.util.Map;
import org.assertj.core.api.Assertions;
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