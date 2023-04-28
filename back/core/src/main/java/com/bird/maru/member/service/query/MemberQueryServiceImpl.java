package com.bird.maru.member.service.query;

import com.bird.maru.domain.model.entity.Member;
import com.bird.maru.member.repository.query.MemberQueryRepository;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberQueryServiceImpl implements MemberQueryService {

    private final MemberQueryRepository memberQueryRepository;

    @Override
    public Member findMember(Long memberId) {
        return memberQueryRepository.findById(memberId)
                                    .orElseThrow(() -> new NoSuchElementException("DB에서 데이터를 찾을 수 없습니다"));
    }

}
