package com.bird.maru.member.service;

import com.bird.maru.cloud.aws.s3.service.AwsS3Service;
import com.bird.maru.common.exception.ResourceNotFoundException;
import com.bird.maru.domain.model.entity.Member;
import com.bird.maru.domain.model.type.Image;
import com.bird.maru.domain.model.type.PointMoney;
import com.bird.maru.member.controller.dto.MemberInfoUpdateDto;
import com.bird.maru.member.repository.MemberRepository;
import com.bird.maru.member.repository.query.MemberQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberQueryRepository memberQueryRepository;
    private final MemberRepository memberRepository;
    private final AwsS3Service awsS3Service;

    @Override
    public Member modifyMemberInfo(Long memberId, MemberInfoUpdateDto memberInfoUpdateDto) {
        Member member = memberQueryRepository.findById(memberId)
                                             .orElseThrow(() -> new ResourceNotFoundException("회원을 찾을 수 없습니다."));

        String nickname = memberInfoUpdateDto.getNickname();
        MultipartFile image = memberInfoUpdateDto.getImage();

        if (StringUtils.hasText(nickname)) {
            member.updateNickname(nickname);
        }

        if (image != null && !image.isEmpty()) {
            Image updatedImage = updateS3Image(image, member);
            member.updateImage(updatedImage);
        }

        return member;
    }

    @Override
    public void registerNoticeToken(Long memberId, String noticeToken) {
        memberRepository.findById(memberId)
                        .orElseThrow(() -> new ResourceNotFoundException("회원을 찾을 수 없습니다."))
                        .changeNoticeToken(noticeToken);
    }

    private Image updateS3Image(MultipartFile image, Member member) {
        Image memberImage = member.getImage();

        if (memberImage.equals(Image.getDefaultMemberProfile())) {
            return awsS3Service.uploadMemberProfileImage(image);
        }

        return awsS3Service.updateMemberProfileImage(image, memberImage);
    }

}
