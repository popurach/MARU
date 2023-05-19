package com.bird.maru;

import com.bird.maru.domain.model.document.TagDoc;
import com.bird.maru.tag.repository.TagElasticSearchRepository;
import com.bird.maru.tag.repository.TagRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

@SpringBootTest
@Slf4j
class CoreApplicationTests {

	@Autowired
	private TagElasticSearchRepository tagElasticSearchRepository;

	@Autowired
	private TagRepository tagRepository;

	@Test
	void contextLoads() {
		tagElasticSearchRepository.saveAll(
				tagRepository.findAll()
							 .stream()
							 .map(TagDoc::new)
							 .collect(Collectors.toList())
		);
	}

}
