package com.bird.maru.common.util;

import static org.assertj.core.api.Assertions.assertThat;

import com.bird.maru.domain.model.type.CustomUserDetails;
import java.util.Collection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

@SpringBootTest
class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("JWT로 인증 객체 생성 테스트")
    void createAuthentication() {
        // given
        String testToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjEiLCJlbWFpbCI6InRlc3RAbmF2ZXIuY29tIiwicHJvdmlkZXIiOiJOQVZFUiIsIm5pY2tuYW1lIjoidGVzdCIsImF1dGhvcml0aWVzIjoiIn0.4l4CW9Bz4IYkhndDThRo1U8xEgimbHMFYDg3leGZ28c";

        // when
        Authentication authentication = jwtUtil.createAuthentication(testToken);

        // then
        CustomUserDetails actual = (CustomUserDetails) authentication.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        assertThat(actual.getId()).isEqualTo(1L);
        assertThat(authorities).isEmpty();
    }

}