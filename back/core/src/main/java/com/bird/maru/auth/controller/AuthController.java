package com.bird.maru.auth.controller;

import com.bird.maru.auth.service.AuthService;
import com.bird.maru.domain.model.type.CustomUserDetails;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * OAuth2 인증에 성공한 경우 Access Token, Refresh Token를 생성한다.
     *
     * @param member OAuth2 인증에 성공한 회원
     * @return 헤더에 Access Token, Refresh Token을 담아서 반환
     */
    @GetMapping("/login/oauth2/token")
    public ResponseEntity<Void> generateToken(@AuthenticationPrincipal CustomUserDetails member) {
        Map<String, String> tokens = authService.generateToken(member);
        return ResponseEntity.ok()
                             .headers(httpHeaders -> httpHeaders.setAll(tokens))
                             .build();
    }

    /**
     * Access Token이 만료되었지만, Refresh Token이 만료되지 않은 경우에 Access Token을 재발급한다.
     *
     * @param member Refresh Token으로 생성한 회원 객체
     * @return 재발급한 Access Token
     * @throws AccessDeniedException Refresh Token이 탈취된 가능성이 있는 경우에 발생
     */
    @GetMapping("/api/auth/access-token")
    public ResponseEntity<Void> regenerateAccessToken(@AuthenticationPrincipal CustomUserDetails member) throws AccessDeniedException {
        String accessToken = authService.regenerateAccessToken(member);
        return ResponseEntity.ok()
                             .headers(httpHeaders -> httpHeaders.add("Access-Token", accessToken))
                             .build();
    }

    /**
     * 사용자의 계정 해킹이 의심될 경우 현재 사용자의 Refresh Token을 제거
     *
     * @param member Refresh Token으로 생성한 회원 객체
     */
    @DeleteMapping("/api/auth/refresh-token")
    public void reportRefreshToken(@AuthenticationPrincipal CustomUserDetails member) {
        authService.reportRefreshToken(member);
    }

}
