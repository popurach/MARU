package com.bird.maru;

import com.bird.maru.domain.model.document.Tag;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

@SpringBootTest
@Slf4j
class CoreApplicationTests {

	@Autowired
	private ElasticsearchOperations elasticsearchOperations;

	@Test
	void contextLoads() {
		Tag tag = new Tag(1L, "태그테스트애그3");
		Tag save = elasticsearchOperations.save(tag);
		log.debug("Tag :: {}", tag);
		log.debug("Tag :: {}", save);
	}

}
