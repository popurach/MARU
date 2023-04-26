package com.bird.maru.common.config;

import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class RedisConfigTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    @DisplayName("Redis 실행 테스트")
    void save() {
        // given
        String testKey = "test";
        String testValue = "test_value";

        // when
        redisTemplate.opsForValue()
                     .set(testKey, testValue);

        // then
        Assertions.assertThat(
                redisTemplate.opsForValue()
                             .get(testKey)
        ).isEqualTo(testValue);
    }

}