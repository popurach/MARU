package com.bird.maru.domain.model.type;

public enum Provider {

    NAVER, KAKAO, GOOGLE;

    public static Provider converter(String provider) {
        for (Provider cur : values()) {
            if (cur.name().equals(provider)) {
                return cur;
            }
        }

        throw new IllegalArgumentException("잘못된 Provider 입니다.");
    }

}
