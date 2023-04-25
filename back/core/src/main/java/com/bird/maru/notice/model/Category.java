package com.bird.maru.notice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum Category {
    AUCTION("경매 알림"), POINT("포인트 알림"), LANDMARK("랜드마크 알림");

    private String title;

}
