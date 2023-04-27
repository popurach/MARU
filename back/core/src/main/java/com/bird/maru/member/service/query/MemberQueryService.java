package com.bird.maru.member.service.query;

import com.bird.maru.domain.model.entity.Member;

public interface MemberQueryService {

    Member findMember(Long memberId);

}
