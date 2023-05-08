package com.bird.maru.point.service;

import com.bird.maru.common.exception.ResourceNotFoundException;
import com.bird.maru.domain.model.entity.Member;
import com.bird.maru.domain.model.type.PointMoney;
import com.bird.maru.member.repository.query.MemberQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PointServiceImpl implements PointService {

    private final MemberQueryRepository memberQueryRepository;

    @Override
    public void landmarkVisiting(Long memberId) {
        Member member = memberQueryRepository.findById(memberId)
                                             .orElseThrow(() -> new ResourceNotFoundException("회원을 찾을 수 없습니다."));
        member.gainPoint(PointMoney.LANDMARK_POINT.getValue());
    }

    @Override
    public void landmarkOccupying(Long memberId) {
        Member member = memberQueryRepository.findById(memberId)
                                             .orElseThrow(() -> new ResourceNotFoundException("회원을 찾을 수 없습니다."));
        member.gainPoint(PointMoney.LANDMARK_OCCUPY_POINT.getValue());
    }

    @Override
    public void landmarkPhoto(Long memberId) {
        Member member = memberQueryRepository.findById(memberId)
                                             .orElseThrow(() -> new ResourceNotFoundException("회원을 찾을 수 없습니다."));
        member.gainPoint(PointMoney.LANDMARK_PHOTO_POINT.getValue());
    }

    @Override
    public void spotMaking(Long memberId) {
        Member member = memberQueryRepository.findById(memberId)
                                             .orElseThrow(() -> new ResourceNotFoundException("회원을 찾을 수 없습니다."));
        member.gainPoint(PointMoney.SPOT_POINT.getValue());
    }

    @Override
    public void photoLike(Long memberId) {
        Member member = memberQueryRepository.findById(memberId)
                                             .orElseThrow(() -> new ResourceNotFoundException("회원을 찾을 수 없습니다."));
        member.gainPoint(PointMoney.LIKE_POINT.getValue());
    }

}
