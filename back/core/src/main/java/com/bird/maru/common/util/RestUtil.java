package com.bird.maru.common.util;

import com.bird.maru.common.filter.dto.GoogleRegistration;
import java.time.Duration;
import java.util.Map;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.web.client.RestTemplate;

public final class RestUtil {

    private static final RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();

    private RestUtil() {
    }

    public static String getAccessTokenOfGoogle(GoogleRegistration googleRegistration, String authCode) {
        Map<String, String> params = googleRegistration.getParams();
        params.put("code", authCode);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        return getRestTemplate()
                .exchange(
                        googleRegistration.getTokenUri(),
                        HttpMethod.POST,
                        new HttpEntity<>(params, headers),
                        new ParameterizedTypeReference<Map<String, String>>() {}
                )
                .getBody()
                .get("access_token");
    }

    public static Map<String, Object> getUserInfo(String userInfoUri, String accessToken) {
        return getRestTemplate()
                .exchange(
                        RequestEntity.get(userInfoUri)
                                     .header("Authorization", "Bearer " + accessToken)
                                     .build(),
                        new ParameterizedTypeReference<Map<String, Object>>() {}
                )
                .getBody();
    }

    private static RestTemplate getRestTemplate() {
        return restTemplateBuilder.setConnectTimeout(Duration.ofSeconds(5L))
                                  .setReadTimeout(Duration.ofSeconds(5L))
                                  .build();
    }

}
