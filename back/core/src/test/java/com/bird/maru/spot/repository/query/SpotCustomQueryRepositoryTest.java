package com.bird.maru.spot.repository.query;

import static org.assertj.core.api.Assertions.assertThat;

import com.bird.maru.common.util.RandomUtil;
import com.bird.maru.domain.model.entity.Member;
import com.bird.maru.domain.model.entity.Scrap;
import com.bird.maru.domain.model.entity.Spot;
import com.bird.maru.domain.model.entity.Tag;
import com.bird.maru.domain.model.type.Coordinate;
import com.bird.maru.domain.model.type.Image;
import com.bird.maru.domain.model.type.Provider;
import com.bird.maru.member.repository.MemberRepository;
import com.bird.maru.scrap.repository.ScrapRepository;
import com.bird.maru.scrap.repository.query.ScrapQueryRepository;
import com.bird.maru.spot.controller.dto.SpotSearchCondition;
import com.bird.maru.spot.repository.SpotRepository;
import com.bird.maru.tag.repository.TagRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureTestEntityManager
@Slf4j
class SpotCustomQueryRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private SpotRepository spotRepository;

    @Autowired
    private ScrapRepository scrapRepository;

    @Autowired
    private ScrapQueryRepository scrapQueryRepository;

    @Autowired
    private SpotCustomQueryRepository spotCustomQueryRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void beforeEach() {
        Member testMember1 = Member.builder()
                                   .nickname("test1")
                                   .email("test1@gmail.com")
                                   .provider(Provider.GOOGLE)
                                   .build();
        memberRepository.save(testMember1);

        Member testMember2 = Member.builder()
                                   .nickname("test2")
                                   .email("test2@naver.com")
                                   .provider(Provider.NAVER)
                                   .build();
        memberRepository.save(testMember2);

        List<Tag> tags = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            tags.add(
                    Tag.builder()
                       .name("dummy " + i)
                       .build()
            );
        }
        tagRepository.saveAll(tags);

        List<Spot> spots = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            spots.add(
                    Spot.builder()
                        .member((i & 1) != 0 ? testMember1 : testMember2)
                        .image(Image.getDefaultMemberProfile())
                        .coordinate(new Coordinate(Math.random() * 90, Math.random() * 90))
                        .build()
            );
        }

        for (Spot cur : spots) {
            cur.addTag(RandomUtil.randomElement(tags));
        }
        spotRepository.saveAll(spots);

        List<Scrap> scraps = new ArrayList<>();
        for (Spot cur : spots) {
            scraps.add(
                    Scrap.builder()
                         .member(cur.getMember() == testMember2 ? testMember1 : testMember2)
                         .spot(cur)
                         .build()
            );
        }
        scrapRepository.saveAll(scraps);
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("내 스팟 목록 조회 테스트")
    void findMySpotsTest() {
        // given
        Member testMember = memberRepository.findAll().get(0);
        long lastOffset = spotRepository.findAll().get(40).getId();
        int pageSize = 18;

        // when
        List<Long> spotIds = spotCustomQueryRepository.findIdsByMemberAndMineCondition(
                testMember.getId(), SpotSearchCondition.builder()
                                                       .lastOffset(lastOffset)
                                                       .size(pageSize)
                                                       .mine(Boolean.TRUE)
                                                       .scraped(Boolean.FALSE)
                                                       .build()
        );
        List<Spot> spots = spotCustomQueryRepository.findAllWithTagsByIdIn(spotIds);

        // then
        assertThat(
                spots.stream()
                     .filter(Predicate.not(Spot::getDeleted))
                     .filter(spot -> spot.getId() < lastOffset && testMember.getId().equals(spot.getMember().getId()))
                     .collect(Collectors.toList())
        ).hasSize(pageSize);
    }

    @Test
    @DisplayName("내 스크랩 목록 조회 테스트")
    void findMyScrapsTest() {
        // given
        Member testMember = memberRepository.findAll().get(0);
        long lastOffset = spotRepository.findAll().get(40).getId();
        int pageSize = 20;

        // when
        List<Spot> scrapedSpots = spotCustomQueryRepository.findAllByMemberAndScrapCondition(
                testMember.getId(), SpotSearchCondition.builder()
                                                       .lastOffset(lastOffset)
                                                       .size(pageSize)
                                                       .mine(Boolean.FALSE)
                                                       .scraped(Boolean.TRUE)
                                                       .build()
        );

        // then
        Set<Long> scraped = scrapQueryRepository.findSpotIdsByMemberAndSpotIn(testMember.getId(), scrapedSpots.stream()
                                                                                                              .map(Spot::getId)
                                                                                                              .collect(Collectors.toList()));
        assertThat(
                scrapedSpots.stream()
                            .filter(Predicate.not(Spot::getDeleted))
                            .filter(spot -> scraped.contains(spot.getId()))
                            .collect(Collectors.toList())
        ).hasSize(pageSize);
    }

}