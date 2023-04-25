package com.bird.maru.auth.service;

import com.bird.maru.domain.model.entity.Member;
import com.bird.maru.domain.model.type.CustomUserDetails;
import com.bird.maru.domain.model.type.Provider;
import com.bird.maru.member.repository.MemberRepository;
import com.bird.maru.member.repository.query.MemberQueryRepository;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Authorization Code Grant 방식을 지원합니다. <br>
 * OAuth2LoginAuthenticationFilter에서 "/login/oauth2/code/*"로 들어오는 요청을 인터셉트하고, state 검증을 한 후에 이 빈을 사용합니다. <br>
 * 이 빈은 Resource Server로부터 받은 사용자 정보를 이용하여 회원 가입을 수행하고 Pricipal을 반환해야 합니다. <br>
 * 이후 OAuth2LoginAuthenticationFilter에서 반환한 Principal로 Authentication 객체를 생성하고 Security Context에 등록하여 인증을 수행합니다.
 */
@Service
@RequiredArgsConstructor
public class AuthCodeUserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;
    private final MemberQueryRepository memberQueryRepository;
    private final DefaultOAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // Resource Server로부터 사용자 정보 조회
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        Map<String, Object> attributes = oAuth2User.getAttributes();
        Provider provider = Provider.convert(
                userRequest.getClientRegistration()
                           .getRegistrationId()
        );

        CustomUserDetails userDetails = CustomUserDetails.of(attributes, provider);
        memberQueryRepository.findByEmailAndProvider(userDetails.getEmail(), userDetails.getProvider())
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
