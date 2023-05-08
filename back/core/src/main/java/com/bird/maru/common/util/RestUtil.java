package com.bird.maru.common.util;

import com.bird.maru.common.filter.dto.GoogleRegistration;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RestUtil {

    public static String getAccessTokenOfGoogle(GoogleRegistration googleRegistration, String authCode) {
        JsonNode body = WebClient.create(googleRegistration.getTokenUri())
                                 .post()
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .bodyValue(googleRegistration.getBody(authCode))
                                 .retrieve()
                                 .bodyToMono(JsonNode.class)
                                 .block();

        if (body == null) {
            return null;
        }

        return body.get("access_token").asText();
    }

    public static Map<String, Object> getUserInfo(String userInfoUri, String accessToken) {
        return WebClient.create(userInfoUri)
                        .get()
                        .header("Authorization", "Bearer " + accessToken)
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                        .block();
    }

}
