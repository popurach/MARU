package com.bird.maru.spot.controller;

import com.bird.maru.auth.service.dto.CustomUserDetails;
import com.bird.maru.common.exception.ResourceConflictException;
import com.bird.maru.common.exception.ResourceNotFoundException;
import com.bird.maru.common.util.NamedLockExecutor;
import com.bird.maru.like.service.LikeService;
import com.bird.maru.spot.controller.dto.SpotDetailResponseDto;
import com.bird.maru.spot.controller.dto.SpotPostRequestDto;
import com.bird.maru.spot.controller.dto.SpotSearchCondition;
import com.bird.maru.spot.repository.query.dto.SpotSimpleDto;
import com.bird.maru.spot.service.SpotService;
import com.bird.maru.spot.service.query.SpotQueryService;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/spots")
@RequiredArgsConstructor
public class SpotController {

    private final SpotQueryService spotQueryService;
    private final SpotService spotService;
    private final LikeService likeService;
    private final NamedLockExecutor namedLockExecutor;

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

    /**
     * 좋아요 개수를 높이는 것은 동시성 문제가 발생할 수 있습니다. 따라서 Named Lock을 이용하여 좋아요 여부를 토글하도록 구현했습니다.
     *
     * @param member 현재 로그인 한 회원
     * @param spotId 회원이 좋아요를 토글하려는 스팟
     */
    @PostMapping("/{spotId}/like")
    public void toggleLike(
            @AuthenticationPrincipal CustomUserDetails member,
            @PathVariable Long spotId
    ) {
        namedLockExecutor.executeWithLock(
                spotId.toString(),
                5,
                () -> likeService.toggleLike(member.getId(), spotId)
        );
    }

    /**
     * 스팟 등록 API <br/> - 사진, 스팟, 태그, 스팟에 대한 사진, 포인트 변화가 하나의 트랜잭션 작업
     *
     * @param spotPostRequestDto : 스팟 등록시 필요한 Request DTO
     * @param member             : 현재 접근중인 주체
     * @return Long : spot의 id
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long postSpot(
            @Valid @ModelAttribute SpotPostRequestDto spotPostRequestDto, @AuthenticationPrincipal CustomUserDetails member
    ) {
        return spotService.insertSpotAndTags(spotPostRequestDto.getSpotImage(),
                                             spotPostRequestDto.getTags(),
                                             spotPostRequestDto.getLandmarkId(),
                                             member.getId());
    }

    /**
     * 스팟 삭제 API
     *
     * @param spotId : 삭제할 spot id
     * @param member : 현재 접근중인 주체
     * @throws ResourceConflictException : 이미 삭제된 리소스를 삭제하려고 하면 발생합니다. - Conflict
     * @throws ResourceNotFoundException : 존재하지 않는 리소스에 접근 시 발생합니다. - NotFound
     */
    @DeleteMapping("{spotId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteSpot(@NotNull @PathVariable Long spotId, @AuthenticationPrincipal CustomUserDetails member)
            throws ResourceConflictException, ResourceNotFoundException {
        spotService.deleteSpot(spotId, member.getId());
    }

    /**
     * 스팟 상세보기 API
     *
     * @param spotId : 스팟 id
     * @param member : 현재 접근중인 주체
     * @return SpotDetailResponseDto : 상세보기 dto
     * @throws ResourceNotFoundException : 리소스 없음
     */
    @GetMapping("{spotId}")
    @ResponseStatus(HttpStatus.OK)
    public SpotDetailResponseDto findSpotDetail(
            @NotNull @PathVariable Long spotId, @AuthenticationPrincipal CustomUserDetails member
    ) throws ResourceNotFoundException {
        return spotQueryService.findSpotDetail(spotId, member.getId());
    }

}
