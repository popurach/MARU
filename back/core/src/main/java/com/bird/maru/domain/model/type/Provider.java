package com.bird.maru.domain.model.type;

public enum Provider {

    NAVER, KAKAO, GOOGLE;

    public static Provider convert(String provider) {
        provider = provider.toUpperCase();

        for (Provider cur : values()) {
            if (cur.name().equals(provider)) {
                return cur;
            }
        }

        throw new IllegalArgumentException("잘못된 Provider 입니다.");
    }

}
