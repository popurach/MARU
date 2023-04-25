package com.bird.maru.common.config;

import com.bird.maru.auth.service.TokenUserService;
import com.bird.maru.common.filter.ImplicitOAuth2LoginAuthenticationFilter;
import com.bird.maru.common.filter.JwtAuthenticationFilter;
import com.bird.maru.common.handler.JwtAccessDeniedHandler;
import com.bird.maru.common.handler.JwtAuthenticationEntryPoint;
import com.bird.maru.common.util.JwtUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
//    private final RedisAuthorizationRequestRepository redisAuthorizationRequestRepository;
//    private final AuthCodeUserService authCodeUserService;
//    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
//    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final TokenUserService tokenUserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // CORS 설정
        http.cors().configurationSource(corsConfigurationSource());

        // JWT를 사용하므로 CSRF Token 검증 관련 기능 비활성화
        http.csrf().disable();

        // JWT를 사용하므로 요청마다 새로운 세션을 생성하는 기능 비활성화
        http.sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Authorization Code Grant 방식의 OAuth2 인증 관련 설정 추가
//        http.oauth2Login(
//                configurer -> configurer.authorizationEndpoint(
//                                                config -> config.authorizationRequestRepository(redisAuthorizationRequestRepository)
//                                        )
//                                        .userInfoEndpoint(
//                                                config -> config.userService(authCodeUserService)
//                                        )
//                                        .successHandler(oAuth2AuthenticationSuccessHandler)
//                                        .failureHandler(oAuth2AuthenticationFailureHandler)
//        );

        // Implicit Grant 방식의 OAuth2 인증 관련 설정 추가
        http.addFilterBefore(
                new ImplicitOAuth2LoginAuthenticationFilter("/login/oauth2/token", tokenUserService),
                UsernamePasswordAuthenticationFilter.class
        );

        // JWT 필터, 인증/인가 실패 핸들러 등록
        http.addFilterAfter(new JwtAuthenticationFilter(jwtUtil), LogoutFilter.class);
        http.exceptionHandling(
                handler -> handler.authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                                  .accessDeniedHandler(new JwtAccessDeniedHandler())
        );

        // 인증/인가 설정 추가
        http.authorizeRequests(
                request -> request.antMatchers("/health").permitAll()
                                  .anyRequest().authenticated()
        );

        return http.build();
    }

    /**
     * @return CORS 관련 설정을 담은 빈을 반환합니다.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration loginCorsConfiguration = getLoginCorsConfiguration();
        CorsConfiguration jwtCorsConfiguration = getJwtCorsConfiguration();

        UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();
        // Authorization Code Grant 방식 지원
//        corsConfigurationSource.registerCorsConfiguration("/login/oauth2/code/*", loginCorsConfiguration);
//        corsConfigurationSource.registerCorsConfiguration("/oauth2/authorization/*", loginCorsConfiguration);

        // Implicit Grant 방식 지원
        corsConfigurationSource.registerCorsConfiguration("/login/oauth2/token/*", loginCorsConfiguration);

        corsConfigurationSource.registerCorsConfiguration("/api/**", jwtCorsConfiguration);
        return corsConfigurationSource;
    }

    /**
     * 안드로이드 개발을 위해 오리진은 모든 오리진을 허용합니다. <br> 로그인을 제외한 모든 API 요청에서 사용하므로 HTTP Method는 GET, POST, PUT, DELETE를 허용합니다. <br> JWT 인증을 위해 헤더에 Authorization을
     * 반드시 포함해야 합니다.
     *
     * @return JWT 인증과 관련한 CORS 설정을 반환
     */
    private CorsConfiguration getJwtCorsConfiguration() {
        CorsConfiguration jwtCorsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
        jwtCorsConfiguration.setAllowedOrigins(List.of("*"));
        jwtCorsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        jwtCorsConfiguration.setAllowedHeaders(List.of("Authorization"));
        return jwtCorsConfiguration;
    }

    /**
     * 안드로이드 개발을 위해 오리진은 모든 오리진을 허용합니다. <br> OAuth2.0 인증 과정에서 사용하므로 HTTP Method는 GET만을 허용합니다. <br> 반드시 필요한 헤더는 없습니다.
     *
     * @return JWT 인증과 무관한 CORS 설정을 반환
     */
    private CorsConfiguration getLoginCorsConfiguration() {
        CorsConfiguration loginCorsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
        loginCorsConfiguration.setAllowedOrigins(List.of("*"));
        loginCorsConfiguration.setAllowedMethods(List.of("GET"));
        loginCorsConfiguration.setAllowedHeaders(List.of("Access-Token"));
        loginCorsConfiguration.setExposedHeaders(List.of("Access-Token", "Refresh-Token"));
        return loginCorsConfiguration;
    }

}
