package com.bird.maru.common.filter;

import com.bird.maru.auth.service.TokenUserService;
import com.bird.maru.domain.model.type.Provider;
import java.util.Collections;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

public class ImplicitOAuth2LoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final TokenUserService tokenUserService;

    public ImplicitOAuth2LoginAuthenticationFilter(String defaultFilterProcessesUrl, TokenUserService tokenUserService) {
        super(defaultFilterProcessesUrl);
        this.tokenUserService = tokenUserService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        try {
            String[] requestInfo = request.getHeader("Access-Token").split(" ");
            Provider provider = Provider.convert(requestInfo[0]);
            String accessToken = requestInfo[1];

            OAuth2User oAuth2User = tokenUserService.loadUser(provider, accessToken);
            return new UsernamePasswordAuthenticationToken(oAuth2User, null, Collections.emptyList());
        }  catch (Exception e) {
            throw new OAuth2AuthenticationException("OAuth2 인증 실패");
        }
    }

}
