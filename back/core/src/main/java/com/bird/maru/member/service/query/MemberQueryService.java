package com.bird.maru.member.service.query;

import com.bird.maru.common.exception.ResourceNotFoundException;
import com.bird.maru.domain.model.entity.Member;

public interface MemberQueryService {

    Member findMember(Long memberId) throws ResourceNotFoundException;

    Long checkVisitLandmark(Long memberId, Long landmarkId);

}
