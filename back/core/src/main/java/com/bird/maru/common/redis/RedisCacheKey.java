package com.bird.maru.common.redis;

public class RedisCacheKey {

    // separate
    public static final String SEPARATOR = ":";

    // Authorization Request Key
    public static final String AUTHORIZATION_REQUEST_KEY = "authorization_request";
    // Refresh Token Key
    public static final String REFRESH_TOKEN_KEY = "member_refresh";

    // Member Landmark Visited Key
    public static final String MEMBER_VISITED = "member_visited";


    public static <T> String createKey(String keyName, T id) {
        StringBuilder sb = new StringBuilder();
        return sb.append(keyName)
                 .append(SEPARATOR)
                 .append(id.toString())
                 .toString();
    }

}
