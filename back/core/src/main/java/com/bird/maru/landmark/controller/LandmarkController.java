package com.bird.maru.landmark.controller;

import com.bird.maru.auth.service.dto.CustomUserDetails;
import com.bird.maru.common.exception.ResourceNotFoundException;
import com.bird.maru.domain.model.entity.Member;
import com.bird.maru.domain.model.entity.Spot;
import com.bird.maru.landmark.controller.dto.LandmarkMapResponseDto;
import com.bird.maru.landmark.controller.dto.LandmarkResponseDto;
import com.bird.maru.landmark.controller.dto.LandmarkSpotResponseDto;
import com.bird.maru.landmark.controller.dto.LandmarkStampResponseDto;
import com.bird.maru.landmark.controller.dto.OwnerResponseDto;
import com.bird.maru.landmark.mapper.LandmarkMapper;
import com.bird.maru.landmark.service.query.LandmarkQueryService;
import com.bird.maru.member.mapper.MemberMapper;
import com.bird.maru.member.service.MemberService;
import com.bird.maru.member.service.query.MemberQueryService;
import com.bird.maru.spot.mapper.SpotMapper;
import com.bird.maru.spot.service.query.SpotQueryService;
import java.util.List;
import javax.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class LandmarkController {

    private final LandmarkQueryService landmarkQueryService;
    private final MemberQueryService memberQueryService;
    private final MemberService memberService;
    private final SpotQueryService spotQueryService;
    private final MemberMapper memberMapper;

    /**
     * 랜드마크 정보 조회 - 이름 반환
     *
     * @param id : pathVariable 로 landmark id 전달
     * @return LandmarkResponseDto
     * @throws ResourceNotFoundException : DB에 해당 리소스 존재하지 않음
     */
    @GetMapping("/landmarks/{id}")
    public LandmarkResponseDto findLandmark(@PathVariable Long id) throws ResourceNotFoundException {
        return LandmarkMapper.toLandmarkResponseDto(landmarkQueryService.findLandmark(id));
    }

    /**
     * 랜드마크 소유자 정보 조회 <br/> 삭제된 사용자이거나 랜드마크 소유자가 없을 경우 디폴트 정보 제공 <br/> 사용자 있다면 소유자의 사진 랜덤하게 함께 제공
     *
     * @param id : pathVariable 로 landmark id 전달
     * @return OwnerResponseDto
     * @throws ResourceNotFoundException : DB에 해당 리소스 존재하지 않음
     */
    @GetMapping("/landmarks/{id}/owner")
    public OwnerResponseDto findOwner(@PathVariable Long id) throws ResourceNotFoundException {
        Long memberId = landmarkQueryService.findLandmark(id).getMemberId();
        Member member = memberQueryService.findMember(memberId);
        if (member.getDeleted().equals(Boolean.TRUE)) {
            return memberMapper.toOwnerResponseDto(
                    memberQueryService.findMember(0L),
                    null
            );
        }
        if (member.getId().equals(0L)) {
            return memberMapper.toOwnerResponseDto(
                    member,
                    null
            );
        }

        return memberMapper.toOwnerResponseDto(
                member,
                spotQueryService.findOwnerSpot(memberId, id)
        );
    }

    /**
     * 랜드마크 지도 기준 검색 - 랜드마크는 클러스터링 진행하지 않습니다. <br/> 랜드마크 정보에는 해당 사용자의 최초 방문 여부를 함께 반환합니다. 결과가 존재하지 않다면 NoContent를 반환하고, <br/> 결과가 존재한다면 좌표 정보와 함께 사용자가
     * 방문했는지 정보를 같이 반환합니다.
     *
     * @param west   : minLng
     * @param south  : minLat
     * @param east   : maxLng
     * @param north  : maxLat
     * @param member : 현재 접근중인 주체
     * @return List<LandmarkMapResponseDto>
     */
    @GetMapping("/landmarks")
    public ResponseEntity<List<LandmarkMapResponseDto>> findLandmarksBasedMap(
            @RequestParam Double west, @RequestParam Double south,
            @RequestParam Double east, @RequestParam Double north,
            @AuthenticationPrincipal CustomUserDetails member
    ) {
        List<LandmarkMapResponseDto> landmarkBasedMap = landmarkQueryService.findLandmarkBasedMap(west, south, east, north, member.getId());
        if (landmarkBasedMap.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(landmarkBasedMap);
        }

    }

    /**
     * 랜드마크 방문 API
     *
     * @param id     : 랜드마크 id
     * @param member : 현재 접근중인 주체
     * @throws ResourceNotFoundException 리소스 없음
     */
    @PostMapping("/landmarks/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Integer visitLandmark(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails member) throws ResourceNotFoundException {
        if (Boolean.TRUE.equals(memberQueryService.checkVisitLandmark(member.getId(), id))) {
            return 0;
        }
        // 1. PointService -> PointServiceImpl -> SimplPointServiceImpl(extends PointServiceImpl)
        // 2. MemberService에서 처리 [ 우선 이 방법 적용 ]
        return memberService.gainPoint(member.getId(), id);
    }

    /**
     * 모든 랜드마크 with 나의 방문 정보 조회
     *
     * @param member : 현재 접근중인 주체
     * @return LandmarkStampResponseDto : 랜드마크 id, 스팟 id, 최신 스팟 사진, 랜드마크 이름
     */
    @GetMapping("/landmarks/my")
    public List<LandmarkStampResponseDto> findLandmarkStamps(
            @AuthenticationPrincipal CustomUserDetails member) {
        return landmarkQueryService.findLandmarkStamps(member.getId());
    }

    /**
     * 랜드마크에 등록된 Spot 사진 조회 <br/> 필요한 데이터 : spotId, imageUrl <br/> 페이지네이션 - 정렬 기준 최신순, pageSize = 20
     *
     * @param landmarkId : 랜드마크 id
     * @param size       : 페이지 내에 포함된 아이템 수
     * @param lastOffset : 이전 페이지의 마지막 아이템의 id
     * @return 랜드마크에 속한 스팟의 사진과 id
     */
    @GetMapping("/landmarks/{landmarkId}/spots")
    public ResponseEntity<List<LandmarkSpotResponseDto>> findLandmarkSpots(
            @PathVariable Long landmarkId, @RequestParam(defaultValue = "20") Integer size, @RequestParam @Nullable Long lastOffset
    ) {
        List<Spot> spots = spotQueryService.findLandmarkSpots(landmarkId, lastOffset, size);
        if (spots.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(SpotMapper.toLandmarkSpotResponseDtos(spots));
        }
    }

}
