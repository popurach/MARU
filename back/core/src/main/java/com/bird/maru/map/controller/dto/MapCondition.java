package com.bird.maru.map.controller.dto;

import com.bird.maru.cluster.geo.BoundingBox;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@SuperBuilder
@ToString(callSuper = true)
public class MapCondition extends BoundingBox {

    private Boolean mine;

}
