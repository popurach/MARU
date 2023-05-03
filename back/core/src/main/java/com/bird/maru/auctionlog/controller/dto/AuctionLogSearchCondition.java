package com.bird.maru.auctionlog.controller.dto;

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
public class AuctionLogSearchCondition {

    private Long lastOffset;

    private Integer size;

    public Integer getSize() {
        return Objects.requireNonNullElse(this.size, 20);
    }

}
