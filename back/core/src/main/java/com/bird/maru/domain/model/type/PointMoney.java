package com.bird.maru.domain.model.type;

public enum PointMoney {
    LANDMARK_POINT(2000), // 랜드마크 방문 포인트
    LANDMARK_OCCUPY_POINT(10000), // 랜드마크 점유 포인트
    LANDMARK_PHOTO_POINT(5000), // 랜드마크 사진 등록 포인트
    SPOT_POINT(1000), // 스팟 생성 포인트
    LIKE_POINT(10); // 좋아요 포인트


    private final int value;

    PointMoney(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}