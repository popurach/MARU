package com.bird.maru.common.filter;

import com.bird.maru.common.util.JwtUtil;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String token = jwtUtil.resolveToken(request);

        if (StringUtils.hasText(token) && jwtUtil.validate(token)) {
            Authentication authentication = jwtUtil.createAuthentication(token);
            SecurityContextHolder.getContext()
                                 .setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

}
