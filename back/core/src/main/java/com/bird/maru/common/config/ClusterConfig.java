package com.bird.maru.common.config;

import com.bird.maru.cluster.util.DistanceMeasure;
import com.bird.maru.cluster.util.EuclideanDistance;
import com.bird.maru.cluster.util.HaversineDistance;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClusterConfig {

    @Bean
    @ConditionalOnProperty(name = "cluster.crs", havingValue = "MERCATOR", matchIfMissing = true)
    public DistanceMeasure euclideanMeasure() {
        return new EuclideanDistance();
    }

    @Bean
    @ConditionalOnProperty(name = "cluster.crs", havingValue = "WGS84")
    public DistanceMeasure haversineMeasure() {
        return new HaversineDistance();
    }

}
