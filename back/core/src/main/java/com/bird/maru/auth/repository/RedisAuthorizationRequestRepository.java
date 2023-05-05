package com.bird.maru.auth.repository;

import com.bird.maru.common.redis.RedisCacheKey;
import java.time.Duration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Repository;

/**
 * Authorization Code Grant 방식을 지원합니다. <br> OAuth2.0 인증 과정에서 서버간 상태를 공유하기 위해 이 빈을 사용합니다. <br> OAuth2AuthorizationRequestRedirectFilter에서
 * "/oauth2/authorization/*"로 들어오는 요청을 인터셉트하고, saveAuthorizationRequest를 호출합니다. <br> OAuth2LoginAuthenticationFilter에서 "/login/oauth2/code/*"로 들어오는
 * 요청을 인터셉트하고, removeAuthorizationRequest를 호출합니다.
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class RedisAuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    private final RedisTemplate<String, OAuth2AuthorizationRequest> redisTemplate;

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        String state = request.getParameter(OAuth2ParameterNames.STATE);
        log.debug("loadAuthorizationRequest의 request: {}, state: {}", request, state);

        if (state == null) {
            return null;
        }

        String redisKey = getRedisKey(state);
        return redisTemplate.opsForValue().get(redisKey);
    }

    @Override
    public void saveAuthorizationRequest(
            OAuth2AuthorizationRequest authorizationRequest,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String state = authorizationRequest.getState();
        log.debug("saveAuthorizationRequest의 request: {}, state: {}", request, state);

        String redisKey = getRedisKey(state);
        redisTemplate.opsForValue().set(redisKey, authorizationRequest, Duration.ofMinutes(5));
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
        String state = request.getParameter(OAuth2ParameterNames.STATE);
        log.debug("removeAuthorizationRequest의 request: {}, state: {}", request, state);

        if (state == null) {
            return null;
        }

        String redisKey = getRedisKey(state);
        return redisTemplate.opsForValue().getAndDelete(redisKey);
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        return removeAuthorizationRequest(request);
    }

    private String getRedisKey(String state) {
        return RedisCacheKey.AUTHORIZATION_REQUEST.getKey(state);
    }

}
