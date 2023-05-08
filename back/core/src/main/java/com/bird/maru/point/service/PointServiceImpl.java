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
    public Integer landmarkVisiting(Long memberId) {
        Member member = memberQueryRepository.findById(memberId)
                                             .orElseThrow(() -> new ResourceNotFoundException("회원을 찾을 수 없습니다."));
        int point = PointMoney.LANDMARK_POINT.getValue();
        member.gainPoint(point);
        return point;
    }

    @Override
    public Integer landmarkOccupying(Long memberId) {
        Member member = memberQueryRepository.findById(memberId)
                                             .orElseThrow(() -> new ResourceNotFoundException("회원을 찾을 수 없습니다."));
        int point =  PointMoney.LANDMARK_OCCUPY_POINT.getValue();
        member.gainPoint(point);
        return point;
    }

    @Override
    public Integer landmarkPhoto(Long memberId) {
        Member member = memberQueryRepository.findById(memberId)
                                             .orElseThrow(() -> new ResourceNotFoundException("회원을 찾을 수 없습니다."));
        int point = PointMoney.LANDMARK_PHOTO_POINT.getValue();
        member.gainPoint(point);
        return point;
    }

    @Override
    public Integer spotMaking(Long memberId) {
        Member member = memberQueryRepository.findById(memberId)
                                             .orElseThrow(() -> new ResourceNotFoundException("회원을 찾을 수 없습니다."));
        int point = PointMoney.SPOT_POINT.getValue();
        member.gainPoint(point);
        return point;
    }

    @Override
    public Integer photoLike(Long memberId) {
        Member member = memberQueryRepository.findById(memberId)
                                             .orElseThrow(() -> new ResourceNotFoundException("회원을 찾을 수 없습니다."));
        int point = PointMoney.LIKE_POINT.getValue();
        member.gainPoint(point);
        return point;
    }

}
