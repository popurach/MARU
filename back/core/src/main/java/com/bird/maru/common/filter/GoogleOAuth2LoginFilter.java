package com.bird.maru.common.filter;

import com.bird.maru.common.filter.dto.GoogleRegistration;
import com.bird.maru.common.util.RestUtil;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class GoogleOAuth2LoginFilter extends OncePerRequestFilter {

    private final GoogleRegistration googleRegistration;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String authCode = request.getHeader("Authorization-Code");

        if (StringUtils.hasText(authCode)) {
            requestAccessTokenOfGoogle(request, authCode);
        }

        filterChain.doFilter(request, response);
    }

    private void requestAccessTokenOfGoogle(HttpServletRequest request, String authCode) {
        String accessToken = RestUtil.getAccessTokenOfGoogle(googleRegistration, authCode);

        if (StringUtils.hasText(accessToken)) {
            request.setAttribute("Access-Token", "Google " + accessToken);
        }
    }

}
