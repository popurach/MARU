package com.bird.maru.domain.model.type;

public enum PointMoney {
    LANDMARK_POINT(5000), // 랜드마크 포인트
    SPOT_POINT(1000); // 스팟 포인트

    private final int value;

    PointMoney(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}