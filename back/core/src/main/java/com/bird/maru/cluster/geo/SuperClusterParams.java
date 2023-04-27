package com.bird.maru.cluster.geo;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class SuperClusterParams {

    @Value("${cluster.minZoom}")
    private Integer minZoom;
    @Value("${cluster.maxZoom}")
    private Integer maxZoom;
    @Value("${cluster.initZoom}")
    private Integer initZoom;
    @Value("${cluster.nodeSize}")
    private Integer nodeSize;
    @Value("${cluster.radius}")
    private int radius;
    @Value("${cluster.extent}")
    private int extent;

}
