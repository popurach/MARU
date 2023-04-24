package com.bird.maru.auth.controller;

import com.bird.maru.auth.service.AuthService;
import com.bird.maru.domain.model.type.CustomUserDetails;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

}
