package com.bird.maru.like.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.bird.maru.common.exception.ResourceNotFoundException;
import com.bird.maru.domain.model.entity.Like;
import com.bird.maru.domain.model.entity.Member;
import com.bird.maru.domain.model.entity.Spot;
import com.bird.maru.domain.model.type.Coordinate;
import com.bird.maru.domain.model.type.Image;
import com.bird.maru.domain.model.type.Provider;
import com.bird.maru.like.repository.LikeRepository;
import com.bird.maru.like.repository.query.LikeQueryRepository;
import com.bird.maru.member.repository.MemberRepository;
import com.bird.maru.spot.repository.SpotRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LikeServiceImplTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SpotRepository spotRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private LikeQueryRepository likeQueryRepository;

    @Autowired
    private LikeService likeService;

    @BeforeEach
    void beforeEach() {
        Member testMember = Member.builder()
                                  .nickname("test")
                                  .email("test@gamil.com")
                                  .provider(Provider.GOOGLE)
                                  .build();
        memberRepository.save(testMember);

        spotRepository.save(
                Spot.builder()
                    .member(testMember)
                    .image(Image.getDefaultMemberProfile())
                    .coordinate(new Coordinate(30.1, 20.78))
                    .build()
        );
    }

    @AfterEach
    void afterEach() {
        likeRepository.deleteAllInBatch();
        spotRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("좋아요 최초 등록 테스트")
    void firstLikeTest() {
        // given
        Member testMember = memberRepository.findAll().get(0);
        Spot testSpot = spotRepository.findAll().get(0);

        // when
        likeService.toggleLike(testMember.getId(), testSpot.getId());

        // then
        assertThat(
                likeQueryRepository.findByMemberAndSpot(testMember.getId(), testSpot.getId())
        ).isPresent();
    }

    @Test
    @DisplayName("좋아요 토글 테스트")
    void likeToggleTest() {
        // given
        Member testMember = memberRepository.findAll().get(0);
        Spot testSpot = spotRepository.findAll().get(0);
        likeRepository.saveAndFlush(
                Like.builder()
                    .member(testMember)
                    .spot(testSpot)
                    .build()
        );

        // when
        likeService.toggleLike(testMember.getId(), testSpot.getId());

        // then
        Like like = likeQueryRepository.findByMemberAndSpot(testMember.getId(), testSpot.getId())
                                       .orElseThrow(() -> new ResourceNotFoundException("해당 리소스를 찾을 수 없습니다."));
        assertThat(like.getDeleted()).isTrue();

        // when
        likeService.toggleLike(testMember.getId(), testSpot.getId());

        // then
        like = likeQueryRepository.findByMemberAndSpot(testMember.getId(), testSpot.getId())
                                       .orElseThrow(() -> new ResourceNotFoundException("해당 리소스를 찾을 수 없습니다."));
        assertThat(like.getDeleted()).isFalse();
    }

}