package com.bird.maru.member.repository;

import com.bird.maru.member.repository.query.MemberRedisQueryRepository;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
@DisplayName("방문 사용자 테스트")
class MemberRedisQueryTest {

    @Autowired
    private MemberRedisQueryRepository memberRedisQueryRepository;

    @Test
    @DisplayName("레디스에서 방문한 랜드마크 조회 테스트 - 존재하는 경우")
    void existVisitedLandmarkTest() {
        // given
        Long memberId = 1L;

        // when
        Set<Long> visitedLandmarks = memberRedisQueryRepository.findVisitedLandmarks(memberId);

        // then
        log.debug("{}", visitedLandmarks);
        Assertions.assertThat(visitedLandmarks).hasSize(5);

    }

    @Test
    @DisplayName("레디스에서 방문한 랜드마크 조회 테스트 - 존재하지 않는 경우")
    void notExistVisitedLandmarkTest() {
        // given
        Long memberId = 999L;

        // when
        Set<Long> visitedLandmarks = memberRedisQueryRepository.findVisitedLandmarks(memberId);

        // then
        log.debug("{}", visitedLandmarks);
        Assertions.assertThat(visitedLandmarks).isEmpty();

    }

//    @Test
//    @DisplayName("사용자 랜드마크 방문 추가 테스트")

}
