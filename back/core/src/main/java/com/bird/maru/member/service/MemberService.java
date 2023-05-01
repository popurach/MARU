package com.bird.maru.member.service;

import com.bird.maru.common.exception.ResourceNotFoundException;
import com.bird.maru.domain.model.entity.Member;
import org.springframework.web.multipart.MultipartFile;

public interface MemberService {

    Member modifyMemberInfo(Long memberId, String nickname, MultipartFile newImage);

    void registerNoticeToken(Long memberId, String noticeToken);

    Integer gainPoint(Long memberId, Long landmarkId) throws ResourceNotFoundException;

}
