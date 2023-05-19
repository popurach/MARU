package com.bird.maru.scrap.service;

import com.bird.maru.domain.model.entity.Scrap;
import com.bird.maru.member.repository.MemberRepository;
import com.bird.maru.member.repository.query.MemberQueryRepository;
import com.bird.maru.scrap.repository.ScrapRepository;
import com.bird.maru.scrap.repository.query.ScrapQueryRepository;
import com.bird.maru.spot.repository.SpotRepository;
import com.bird.maru.spot.repository.query.SpotQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ScrapServiceImpl implements ScrapService {

    private final MemberQueryRepository memberQueryRepository;
    private final SpotQueryRepository spotQueryRepository;
    private final ScrapRepository scrapRepository;
    private final ScrapQueryRepository scrapQueryRepository;

    @Override
    public void toggleScrap(Long memberId, Long spotId) {
        scrapQueryRepository.findByMemberAndSpot(memberId, spotId)
                            .ifPresentOrElse(
                                    Scrap::toggleDeleted,
                                    () -> scrapRepository.save(
                                            Scrap.builder()
                                                 .member(memberQueryRepository.getReferenceById(memberId))
                                                 .spot(spotQueryRepository.getReferenceById(spotId))
                                                 .build()
                                    )
                            );
    }

}
