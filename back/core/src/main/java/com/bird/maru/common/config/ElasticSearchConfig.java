package com.bird.maru.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;

@Configuration
public class ElasticSearchConfig extends ElasticsearchConfiguration {

    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                                  .connectedTo("k8a4031.p.ssafy.io:8080")
                                  .withBasicAuth("maru", "dnflahenQkdlxld!@34")
                                  .build();
    }

}
