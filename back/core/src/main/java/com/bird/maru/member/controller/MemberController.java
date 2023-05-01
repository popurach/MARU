package com.bird.maru.member.controller;

import com.bird.maru.auth.service.dto.CustomUserDetails;
import com.bird.maru.member.controller.dto.MemberInfoDto;
import com.bird.maru.member.mapper.MemberMapper;
import com.bird.maru.member.service.MemberService;
import com.bird.maru.member.service.query.MemberQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberQueryService memberQueryService;
    private final MemberService memberService;
    private final MemberMapper mapper;

    @GetMapping("/my")
    public MemberInfoDto getMemberInfo(@AuthenticationPrincipal CustomUserDetails member) {
        return mapper.toMemberInfoDto(
                memberQueryService.findMember(member.getId())
        );
    }

    @PostMapping("/notice-token")
    public void registerNoticeToken(
            @AuthenticationPrincipal CustomUserDetails member,
            @RequestBody String noticeToken
    ) {
        memberService.registerNoticeToken(member.getId(), noticeToken);
    }

    @PostMapping("/my")
    public MemberInfoDto modifyMemberInfo(
            @AuthenticationPrincipal CustomUserDetails member,
            @RequestPart(required = false) MultipartFile image,
            @RequestBody(required = false) String nickname
    ) {
        return mapper.toMemberInfoDto(
                memberService.modifyMemberInfo(member.getId(), nickname, image)
        );
    }

}
