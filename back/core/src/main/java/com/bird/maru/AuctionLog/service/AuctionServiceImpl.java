package com.bird.maru.AuctionLog.service;

import com.bird.maru.auction.repository.AuctionRepository;
import com.bird.maru.domain.model.entity.Auction;
import com.bird.maru.domain.model.entity.AuctionLog;
import com.bird.maru.domain.model.entity.Landmark;
import com.bird.maru.domain.model.entity.Member;
import com.bird.maru.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuctionServiceImpl implements AuctionService{
    private final AuctionRepository auctionRepository;
    private final MemberRepository memberRepository;

    @Override
    public void auctionExecute(AuctionLog auctionLog) {
        Landmark landmarks = auctionLog.getAuction().getLandmark();
        Auction auction = auctionRepository.findByLandmarkIdAndFinished(landmarks.getId(), false);

        if(auction.getLastLogId().equals(auctionLog.getId())) { //낙찰자
            log.info("낙찰자 : {}", auctionLog.getMember().getId());
            landmarks.setMemberId(auctionLog.getMember().getId()); // 랜드마크 대표 회원 정보 업데이트
            auction.setFinished(true); // 경매 현재 상태 -> 끝남
        } else { // 유찰자
            int price = auctionLog.getPrice(); // 유찰자의 입찰 가격
            Member member = memberRepository.getReferenceById(auctionLog.getMember().getId()); // 유찰자의 객체
            Member updateMember = member.builder()
                    .id(member.getId())
                    .nickname(member.getNickname())
                    .point(member.getPoint() + price) // 유찰자 돈 환불
                    .image(member.getImage())
                    .email(member.getEmail())
                    .provider(member.getProvider())
                    .deleted(member.getDeleted())
                    .noticeToken(member.getNoticeToken())
                    .build();
            memberRepository.save(updateMember);
        }
    }
}
