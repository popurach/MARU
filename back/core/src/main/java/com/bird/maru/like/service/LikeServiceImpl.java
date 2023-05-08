package com.bird.maru.like.service;

import com.bird.maru.domain.model.entity.Like;
import com.bird.maru.like.repository.LikeRepository;
import com.bird.maru.like.repository.query.LikeQueryRepository;
import com.bird.maru.member.repository.MemberRepository;
import com.bird.maru.spot.repository.SpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final MemberRepository memberRepository;
    private final SpotRepository spotRepository;
    private final LikeRepository likeRepository;
    private final LikeQueryRepository likeQueryRepository;

    @Override
    public void toggleLike(Long memberId, Long spotId) {
        likeQueryRepository.findByMemberAndSpot(memberId, spotId)
                           .ifPresentOrElse(
                                   Like::toggleDeleted,
                                   () -> likeRepository.save(
                                                               Like.builder()
                                                                   .member(memberRepository.getReferenceById(memberId))
                                                                   .spot(spotRepository.getReferenceById(spotId))
                                                                   .build()
                                                       )
                                                       .getSpot()
                                                       .likeCountUp()
                           );
    }

}
