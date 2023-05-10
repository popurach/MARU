package com.bird.maru.common.config.WebSocket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bid {

    private Integer price;

    private Long landmarkId;

}
