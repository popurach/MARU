package com.bird.maru.common.redis;

public enum RedisCacheKey {
    AUTHORIZATION_REQUEST("authorization_request"),
    REFRESH_TOKEN("member_refresh"),
    MEMBER_VISITED("member_visited");

    private static final String SEPARATOR = ":";
    private final String keyName;

    RedisCacheKey(String keyName) {
        this.keyName = keyName;
    }

    public String getKey(Object id) {
        return keyName + SEPARATOR + id.toString();
    }
}