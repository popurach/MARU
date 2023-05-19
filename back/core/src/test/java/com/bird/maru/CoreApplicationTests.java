package com.bird.maru;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

@SpringBootTest
@Slf4j
class CoreApplicationTests {

	@Autowired
	private ElasticsearchOperations elasticsearchOperations;

	@Test
	void contextLoads() {
	}

}
