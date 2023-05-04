package com.bird.maru.tag.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.bird.maru.domain.model.entity.Member;
import com.bird.maru.domain.model.entity.Spot;
import com.bird.maru.domain.model.entity.SpotHasTag;
import com.bird.maru.domain.model.entity.Tag;
import com.bird.maru.domain.model.type.Coordinate;
import com.bird.maru.domain.model.type.Image;
import com.bird.maru.domain.model.type.Provider;
import com.bird.maru.member.repository.MemberRepository;
import com.bird.maru.spot.controller.dto.TagRequestDto;
import com.bird.maru.spot.repository.SpotRepository;
import com.bird.maru.tag.repository.query.TagQueryRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Slf4j
class TagJDBCRepositoryTest {

    @Autowired
    private SpotHasTagRepository spotHasTagRepository;

    @Autowired
    private TagQueryRepository tagQueryRepository;

    @Autowired
    private TagJDBCRepository tagJDBCRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private SpotRepository spotRepository;

    private final List<TagRequestDto> tags = new ArrayList<>();
    private Spot spot;
    private Member member;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                       .email("shoebill@maru.com")
                       .point(9999)
                       .provider(Provider.GOOGLE)
                       .image(Image.getDefaultMemberProfile())
                       .nickname("슈빌")
                       .build();
        memberRepository.save(member);
        spot = Spot.builder()
                   .coordinate(Coordinate.builder()
                                         .lng(126.5166)
                                         .lat(36.5145)
                                         .build())
                   .image(Image.getDefaultMemberProfile())
                   .member(member)
                   .build();
        spotRepository.save(spot);
        for (int i = 0; i < 5; i++) {
            tags.add(TagRequestDto.builder()
                                  .name("태그명" + i)
                                  .build());
        }
        tagJDBCRepository.bulkInsertTags(tags);
    }

    @Test
    @DisplayName("태그 bulk insert 테스트")
    @Disabled
    void tagBulkInsertTest() {
        // given
        int totalSize = 5;

        // when
        tagJDBCRepository.bulkInsertTags(tags);
        List<Tag> newTags = tagQueryRepository.findAllByNames(tags.stream().map(TagRequestDto::getName).collect(Collectors.toList()));

        // then
        assertThat(newTags).hasSize(totalSize);
        for (int i = 1; i <= totalSize; i++) {
            assertThat(newTags.get(i - 1).getId()).isEqualTo((long) i);
        }
    }

    @Test
    @DisplayName("스팟 태그 테이블 bulk insert 테스트")
    void spotHasTagBulkInsertTest() {
        // given
        Long spotId = 1L;
        List<Long> tagIds = List.of(1L, 2L, 3L, 4L);

        // when
        tagJDBCRepository.bulkInsertSpotHasTags(spotId, tagIds);
        List<SpotHasTag> all = spotHasTagRepository.findAll();

        // then
        assertThat(all).hasSize(tagIds.size());
    }

}
