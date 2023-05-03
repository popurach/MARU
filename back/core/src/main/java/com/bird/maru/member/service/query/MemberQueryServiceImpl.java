package com.bird.maru.member.service.query;

import com.bird.maru.common.exception.ResourceNotFoundException;
import com.bird.maru.domain.model.entity.Member;
import com.bird.maru.member.repository.query.MemberQueryRepository;
import com.bird.maru.member.repository.query.MemberRedisRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberQueryServiceImpl implements MemberQueryService {

    private final MemberQueryRepository memberQueryRepository;
    private final MemberRedisRepository memberRedisRepository;

    @Override
    public Member findMember(Long memberId) throws ResourceNotFoundException {
        return memberQueryRepository.findById(memberId)
                                    .orElseThrow(() -> new ResourceNotFoundException("DB에서 데이터를 찾을 수 없습니다"));
    }

    /**
     * Redis 방문 여부 조회를 통해 매일 최초 방문 여부 탐색
     *
     * @param memberId   : 현재 접근중인 사용자
     * @param landmarkId : 랜드마크 번호
     * @return Long - 추가된 데이터 수
     */
    @Override
    public Boolean checkVisitLandmark(Long memberId, Long landmarkId) {
        Set<Long> visitedLandmarks = memberRedisRepository.findVisitedLandmarks(memberId);
        if (visitedLandmarks.contains(landmarkId)) {
            return Boolean.TRUE;
        }
        return memberRedisRepository.insertVisitLandmark(memberId, landmarkId) != 0 ? Boolean.FALSE : Boolean.TRUE;
    }

}
