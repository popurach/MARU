package com.bird.maru.common.filter.dto;

import java.util.Map;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class GoogleRegistration {

    private final String clientId;

    private final String clientSecret;

    private final String redirectUri;

    private final String tokenUri;

    public GoogleRegistration(
            @Value("spring.security.oauth2.client.registration.google.client-id") String clientId,
            @Value("spring.security.oauth2.client.registration.google.client-secret") String clientSecret,
            @Value("spring.security.oauth2.client.registration.google.redirect-uri") String redirectUri,
            @Value("spring.security.oauth2.client.provider.google.token-uri") String tokenUri
    ) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.tokenUri = tokenUri;
    }

    public Map<String, String> getParams() {
        return Map.of(
                "client_id", this.clientId,
                "client_secret", this.clientSecret,
                "redirect_uri", this.redirectUri,
                "grant_type", "authorization_code"
        );
    }

}
