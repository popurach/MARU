package com.bird.maru.domain.model.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class CustomUserDetails implements UserDetails, OAuth2User {

    public static final String EMAIL_PATTERN = "email";

    private Long id;

    private String email;

    private Provider provider;

    private String nickname;

    @Builder.Default
    private List<? extends GrantedAuthority> authorities = new ArrayList<>();

    @Builder.Default
    private boolean deleted = false;

    public static CustomUserDetails of(Map<String, Object> attributes, Provider provider) {
        switch (provider) {
            case KAKAO:
                return ofKakao(attributes);
            case NAVER:
                return ofNaver(attributes);
            case GOOGLE:
                return ofGoogle(attributes);
            default:
                throw new RuntimeException("이 코드는 실행될 수 없습니다.");
        }
    }

    @SuppressWarnings("unchecked")
    private static CustomUserDetails ofKakao(Map<String, Object> attributes) {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        return CustomUserDetails.builder()
                                .provider(Provider.KAKAO)
                                .email((String) account.get(EMAIL_PATTERN))
                                .nickname((String) profile.get("nickname"))
                                .build();
    }

    @SuppressWarnings("unchecked")
    private static CustomUserDetails ofNaver(Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return CustomUserDetails.builder()
                                .provider(Provider.NAVER)
                                .email((String) response.get(EMAIL_PATTERN))
                                .nickname((String) response.get("name"))
                                .build();
    }

    private static CustomUserDetails ofGoogle(Map<String, Object> attributes) {
        return CustomUserDetails.builder()
                                .provider(Provider.GOOGLE)
                                .email((String) attributes.get(EMAIL_PATTERN))
                                .nickname((String) attributes.get("name"))
                                .build();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    public String getEmail() {
        return this.email;
    }

    public Provider getProvider() {
        return this.provider;
    }

    @Override
    public String getPassword() {
        return null;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.unmodifiableList(this.authorities);
    }

    public String getStringAuthorities() {
        return this.authorities.stream()
                               .map(GrantedAuthority::getAuthority)
                               .collect(Collectors.joining(","));
    }

    @Override
    public boolean isAccountNonExpired() {
        return !this.deleted;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.deleted;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !this.deleted;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of(
                EMAIL_PATTERN, this.email,
                "provider", this.provider,
                "nickname", this.nickname
        );
    }

    @Override
    public String getName() {
        return this.email;
    }

}
