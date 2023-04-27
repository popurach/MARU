package com.bird.maru.member.controller;

import com.bird.maru.domain.model.type.CustomUserDetails;
import com.bird.maru.member.controller.dto.MemberInfoDto;
import com.bird.maru.member.mapper.MemberMapper;
import com.bird.maru.member.service.query.MemberQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberQueryService memberQueryService;
    private final MemberMapper mapper;

    @GetMapping("/my")
    public MemberInfoDto getMemberInfo(@AuthenticationPrincipal CustomUserDetails member) {
        return mapper.toMemberInfoDto(
                memberQueryService.findMember(member.getId())
        );
    }

}
