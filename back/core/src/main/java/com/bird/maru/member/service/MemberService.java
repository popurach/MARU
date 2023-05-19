package com.bird.maru.member.service;

import com.bird.maru.domain.model.entity.Member;
import com.bird.maru.member.controller.dto.MemberInfoUpdateDto;

public interface MemberService {

    Member modifyMemberInfo(Long memberId, MemberInfoUpdateDto memberInfoUpdateDto);

    void registerNoticeToken(Long memberId, String noticeToken);

}
