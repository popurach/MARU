package com.bird.maru.spot.controller.dto;

import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
@ToString
@EqualsAndHashCode
public class SpotSearchCondition {

    private Long lastOffset;

    private Integer size;

    private Boolean mine;

    private Boolean scraped;

    public Integer getSize() {
        return Objects.requireNonNullElse(this.size, 20);
    }

    public void setMySpotCondition() {
        this.mine = Boolean.TRUE;
        this.scraped = Boolean.FALSE;
    }

    public void setMyScrapCondition() {
        this.mine = Boolean.FALSE;
        this.scraped = Boolean.TRUE;
    }

}
