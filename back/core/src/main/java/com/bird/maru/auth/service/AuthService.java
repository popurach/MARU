package com.bird.maru.auth.service;

import com.bird.maru.domain.model.type.CustomUserDetails;
import java.util.Map;
import org.springframework.security.access.AccessDeniedException;

public interface AuthService {

    Map<String, String> generateToken(CustomUserDetails member);

    String regenerateAccessToken(CustomUserDetails member) throws AccessDeniedException;

    void reportRefreshToken(CustomUserDetails member);

}
