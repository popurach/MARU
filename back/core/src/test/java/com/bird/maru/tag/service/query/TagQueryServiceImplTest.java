package com.bird.maru.tag.service.query;

import static org.assertj.core.api.Assertions.assertThat;

import com.bird.maru.domain.model.document.TagDoc;
import com.bird.maru.domain.model.entity.Member;
import com.bird.maru.domain.model.entity.Spot;
import com.bird.maru.domain.model.entity.SpotHasTag;
import com.bird.maru.domain.model.entity.Tag;
import com.bird.maru.domain.model.type.Coordinate;
import com.bird.maru.domain.model.type.Image;
import com.bird.maru.domain.model.type.Provider;
import com.bird.maru.member.repository.MemberRepository;
import com.bird.maru.spot.repository.SpotRepository;
import com.bird.maru.tag.repository.SpotHasTagRepository;
import com.bird.maru.tag.repository.TagElasticSearchRepository;
import com.bird.maru.tag.repository.TagRepository;
import com.bird.maru.tag.repository.query.TagElasticSearchQueryRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

@SpringBootTest
class TagQueryServiceImplTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SpotRepository spotRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private SpotHasTagRepository spotHasTagRepository;

    @Autowired
    private TagElasticSearchRepository tagElasticSearchRepository;

    @Autowired
    private TagElasticSearchQueryRepository tagElasticSearchQueryRepository;

    @BeforeEach
    void beforeEach() {
        Member testMember = memberRepository.save(
                Member.builder()
                      .nickname("test")
                      .email("test@naver.com")
                      .provider(Provider.NAVER)
                      .build()
        );

        Spot testSpot = spotRepository.save(
                Spot.builder()
                    .member(testMember)
                    .image(Image.getDefaultMemberProfile())
                    .coordinate(new Coordinate(30.1, 20.1))
                    .build()
        );

        List<Tag> tags = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Tag tag = tagRepository.save(
                    Tag.builder()
                       .name("testTag" + i)
                       .build()
            );
            tags.add(tag);
        }

        tagElasticSearchRepository.saveAll(
                tags.stream()
                    .map(TagDoc::new)
                    .collect(Collectors.toList())
        );

        for (Tag tag : tags) {
            spotHasTagRepository.save(
                    SpotHasTag.builder()
                              .spot(testSpot)
                              .tag(tag)
                              .build()
            );
        }
    }

    @Test
    @DisplayName("유사한 태그 검색 테스트")
    void searchTagsTest() {
        List<TagDoc> tagDocs = tagElasticSearchQueryRepository.searchTags("t", Pageable.ofSize(5));
        assertThat(tagDocs).hasSize(4);
    }

}