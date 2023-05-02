package com.bird.maru.common.redis;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
class KeyGeneratorTest {

    @Test
    @DisplayName("레디스 키 생성 테스트")
    void keyGeneratorTest() {
        //given
        Long memberId = 1L;
        String state = "state";

        // when
        String rtkkey = RedisCacheKey.REFRESH_TOKEN.getKey(memberId);
        String visitedKey = RedisCacheKey.MEMBER_VISITED.getKey(memberId);
        String authKey = RedisCacheKey.AUTHORIZATION_REQUEST.getKey(state);

        // then
        Assertions.assertThat(rtkkey).isEqualTo("member_refresh:1");
        Assertions.assertThat(visitedKey).isEqualTo("member_visited:1");
        Assertions.assertThat(authKey).isEqualTo("authorization_request:state");
    }

}
