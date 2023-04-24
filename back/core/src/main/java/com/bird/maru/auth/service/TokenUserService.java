package com.bird.maru.auth.service;

import com.bird.maru.domain.model.entity.Member;
import com.bird.maru.domain.model.type.CustomUserDetails;
import com.bird.maru.domain.model.type.Provider;
import com.bird.maru.member.repository.MemberRepository;
import java.time.Duration;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
public class TokenUserService {

    private final MemberRepository memberRepository;
    private final String googleUserInfoUri;
    private final String naverUserInfoUri;
    private final String kakaoUserInfoUri;

    public TokenUserService(
            MemberRepository memberRepository,
            @Value("${user-info-uri.google}") String googleUserInfoUri,
            @Value("${user-info-uri.naver}") String naverUserInfoUri,
            @Value("${user-info-uri.kakao}") String kakaoUserInfoUri
    ) {
        this.memberRepository = memberRepository;
        this.naverUserInfoUri = naverUserInfoUri;
        this.kakaoUserInfoUri = kakaoUserInfoUri;
        this.googleUserInfoUri = googleUserInfoUri;
    }

    @Transactional
    public OAuth2User loadUser(Provider provider, String accessToken) {
        switch (provider) {
            case GOOGLE:
                return loadUserByGoogle(accessToken);
            case NAVER:
                return loadUserByNaver(accessToken);
            case KAKAO:
                return loadUserByKakao(accessToken);
            default:
                throw new RuntimeException("이 코드는 실행될 수 없습니다.");
        }
    }

    private OAuth2User loadUserByGoogle(String accessToken) {
        Map<String, Object> attributes = getUserInfo(googleUserInfoUri, accessToken);
        return createUserDetails(attributes, Provider.GOOGLE);
    }

    private OAuth2User loadUserByNaver(String accessToken) {
        Map<String, Object> attributes = getUserInfo(naverUserInfoUri, accessToken);
        return createUserDetails(attributes, Provider.NAVER);
    }

    private OAuth2User loadUserByKakao(String accessToken) {
        Map<String, Object> attributes = getUserInfo(kakaoUserInfoUri, accessToken);
        return createUserDetails(attributes, Provider.KAKAO);
    }

    private Map<String, Object> getUserInfo(String userInfoUri, String accessToken) {
        return getRestTemplate(accessToken)
                .exchange(RequestEntity.get(userInfoUri).build(),
                        new ParameterizedTypeReference<Map<String, Object>>() {})
                .getBody();
    }

    private RestTemplate getRestTemplate(String accessToken) {
        return new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofSeconds(5L))
                .setReadTimeout(Duration.ofSeconds(5L))
                .defaultHeader("Authorization", "Bearer " + accessToken)
                .build();
    }

    private OAuth2User createUserDetails(Map<String, Object> attributes, Provider provider) {
        CustomUserDetails userDetails = CustomUserDetails.of(attributes, provider);
        memberRepository.findByEmailAndProvider(userDetails.getEmail(), userDetails.getProvider())
                        .ifPresentOrElse(
                                member -> userDetails.setId(member.getId()),
                                () -> join(userDetails)
                        );
        return userDetails;
    }

    private void join(CustomUserDetails userDetails) {
        Member member = Member.builder()
                              .nickname(userDetails.getNickname())
                              .email(userDetails.getEmail())
                              .provider(userDetails.getProvider())
                              .build();

        memberRepository.save(member);
        userDetails.setId(member.getId());
    }

}
