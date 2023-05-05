package com.bird.maru.common.util;

import static org.assertj.core.api.Assertions.assertThat;

import com.bird.maru.auth.service.dto.CustomUserDetails;
import com.bird.maru.domain.model.type.Provider;
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
        CustomUserDetails testMember = CustomUserDetails.builder()
                                                        .id(1L)
                                                        .nickname("test")
                                                        .provider(Provider.NAVER)
                                                        .email("test@naver.com")
                                                        .build();
        String testToken = jwtUtil.generateRefreshToken(testMember);

        // when
        Authentication authentication = jwtUtil.createAuthentication(testToken);

        // then
        CustomUserDetails actual = (CustomUserDetails) authentication.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        assertThat(actual.getId()).isEqualTo(1L);
        assertThat(authorities).isEmpty();
    }

}