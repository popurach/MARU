package com.bird.maru.common.util;

import com.bird.maru.domain.model.type.CustomUserDetails;
import com.bird.maru.domain.model.type.Provider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.security.Key;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class JwtUtil {

    public static final String TOKEN_PATTERN = "Bearer ";
    public static final String AUTHORITIES_CLAIM_KEY = "authorities";

    private final long accessTokenExpirationTime;
    private final long refreshTokenExpirationTime;
    private final Key key;

    public JwtUtil(
            @Value("${jwt.expiration.access}") long accessTokenExpirationTime,
            @Value("${jwt.expiration.refresh}") long refreshTokenExpirationTime,
            @Value("${jwt.secret}") String secret
    ) {
        this.accessTokenExpirationTime = accessTokenExpirationTime;
        this.refreshTokenExpirationTime = refreshTokenExpirationTime;
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public long getRefreshTokenExpirationTime() {
        return refreshTokenExpirationTime;
    }

    public String generateAccessToken(CustomUserDetails principal) {
        return generateToken(principal, accessTokenExpirationTime);
    }

    public String generateRefreshToken(CustomUserDetails principal) {
        return generateToken(principal, refreshTokenExpirationTime);
    }

    public String resolveToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");

        if (!StringUtils.hasText(authorization) || authorization.startsWith(TOKEN_PATTERN)) {
            return null;
        }

        return authorization.substring(TOKEN_PATTERN.length());
    }

    public boolean validate(String jwt) {
        try {
            getClaims(jwt);
            return true;
        } catch (MalformedJwtException | IllegalArgumentException | UnsupportedJwtException e) {
            log.debug("JWT의 형식이 아닙니다.");
        } catch (ExpiredJwtException e) {
            log.debug("만료된 토큰입니다.");
        } catch (SignatureException e) {
            log.debug("JWT 서명이 일치하지 않습니다.");
        } catch (Exception e) {
            log.debug("유효하지 않은 토큰입니다.");
        }

        return false;
    }

    public Authentication createAuthentication(String jwt) {
        Claims claims = getClaims(jwt);
        List<? extends GrantedAuthority> authorities = getAuthorities(claims);

        CustomUserDetails member = CustomUserDetails.builder()
                                                    .id(claims.get("id", Long.class))
                                                    .email(claims.get("email", String.class))
                                                    .provider(Provider.convert(
                                                            claims.get("provider", String.class)
                                                    ))
                                                    .nickname(claims.get("nickname", String.class))
                                                    .authorities(authorities)
                                                    .build();

        return new UsernamePasswordAuthenticationToken(member, jwt, authorities);
    }

    private String generateToken(CustomUserDetails principal, long expirationTime) {
        return Jwts.builder()
                   .claim("id", principal.getId())
                   .claim("email", principal.getEmail())
                   .claim("provider", principal.getProvider())
                   .claim("nickname", principal.getNickname())
                   .claim(AUTHORITIES_CLAIM_KEY, principal.getStringAuthorities())
                   .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                   .signWith(this.key)
                   .compact();
    }

    private List<? extends GrantedAuthority> getAuthorities(Claims claims) {
        String[] authorities = claims.get(AUTHORITIES_CLAIM_KEY, String.class)
                                     .split(",");

        if (!StringUtils.hasText(authorities[0])) {
            return Collections.emptyList();
        }

        return Arrays.stream(authorities)
                     .map(SimpleGrantedAuthority::new)
                     .collect(Collectors.toList());
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                   .setSigningKey(this.key)
                   .build()
                   .parseClaimsJws(token)
                   .getBody();
    }

}
