package com.bird.maru.landmark.controller;

import com.bird.maru.common.exception.ResourceNotFoundException;
import com.bird.maru.domain.model.entity.Member;
import com.bird.maru.landmark.controller.dto.LandmarkResponseDto;
import com.bird.maru.landmark.controller.dto.OwnerResponseDto;
import com.bird.maru.landmark.mapper.LandmarkMapper;
import com.bird.maru.landmark.service.query.LandmarkQueryService;
import com.bird.maru.member.mapper.MemberMapper;
import com.bird.maru.member.service.query.MemberQueryService;
import com.bird.maru.spot.service.query.SpotQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/landmarks")
public class LandmarkController {

    private final LandmarkQueryService landmarkQueryService;
    private final MemberQueryService memberQueryService;
    private final SpotQueryService spotQueryService;
    private final MemberMapper memberMapper;

    /**
     * 랜드마크 정보 조회 - 이름 반환
     *
     * @param id : pathVariable 로 landmark id 전달
     * @return LandmarkResponseDto
     * @throws ResourceNotFoundException : DB에 해당 리소스 존재하지 않음
     */
    @GetMapping("/{id}")
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
    @GetMapping("/{id}/owner")
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

}
