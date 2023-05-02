package com.bird.maru.spot.controller;

import com.bird.maru.auth.service.dto.CustomUserDetails;
import com.bird.maru.spot.controller.dto.SpotSearchCondition;
import com.bird.maru.spot.repository.query.dto.SpotSimpleDto;
import com.bird.maru.spot.service.query.SpotQueryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/spots")
@RequiredArgsConstructor
public class SpotController {

    private final SpotQueryService spotQueryService;

    /**
     * 내 스팟 목록 조회에 성공할 경우 스팟 목록과 상태 코드 200을 반환합니다.
     *
     * @param member    현재 로그인 한 회원
     * @param condition 내 스팟 목록을 조회하기 위한 조건 (last offset, page size)
     * @return 페이징 처리된 내 스팟 목록
     */
    @GetMapping("/my")
    public List<SpotSimpleDto> findMySpots(
            @AuthenticationPrincipal CustomUserDetails member,
            @ModelAttribute SpotSearchCondition condition
    ) {
        condition.setMySpotCondition();
        return spotQueryService.findMySpots(member.getId(), condition);
    }

    /**
     * 내 스크랩 목록 조회에 성공할 경우 스크랩 목록과 상태 코드 200을 반환합니다.
     *
     * @param member    현재 로그인 한 회원
     * @param condition 내 스크랩 목록을 조회하기 위한 조건(last offset, page size)
     * @return 페이징 처리된 스크랩 목록
     */
    @GetMapping("/my/scraps")
    public List<SpotSimpleDto> findMyScraps(
            @AuthenticationPrincipal CustomUserDetails member,
            @ModelAttribute SpotSearchCondition condition
    ) {
        condition.setMyScrapCondition();
        return spotQueryService.findMyScraps(member.getId(), condition);
    }

}
