package com.bird.maru.map.service.query;

import com.bird.maru.map.controller.dto.MapCondition;
import java.util.List;
import org.wololo.geojson.Feature;

public interface MapQueryService {

    List<Feature> spotsCluster(Long memberId, MapCondition condition);

}
