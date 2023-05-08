package com.bird.maru.spot.service;

import com.bird.maru.cloud.aws.s3.service.AwsS3Service;
import com.bird.maru.common.exception.ResourceConflictException;
import com.bird.maru.common.exception.ResourceNotFoundException;
import com.bird.maru.domain.model.entity.Spot;
import com.bird.maru.domain.model.entity.Tag;
import com.bird.maru.domain.model.type.PointMoney;
import com.bird.maru.landmark.repository.query.LandmarkQueryRepository;
import com.bird.maru.member.repository.query.MemberQueryRepository;
import com.bird.maru.member.service.MemberService;
import com.bird.maru.spot.controller.dto.TagRequestDto;
import com.bird.maru.spot.mapper.SpotMapper;
import com.bird.maru.spot.repository.SpotRepository;
import com.bird.maru.spot.service.dto.SpotImage;
import com.bird.maru.tag.repository.TagJDBCRepository;
import com.bird.maru.tag.repository.query.TagQueryRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SpotServiceImpl implements SpotService {

    private final AwsS3Service awsS3Service;
    private final MemberService memberService;
    private final LandmarkQueryRepository landmarkQueryRepository;
    private final MemberQueryRepository memberQueryRepository;
    private final TagQueryRepository tagQueryRepository;
    private final TagJDBCRepository tagJDBCRepository;
    private final SpotRepository spotRepository;

    /**
     * 스팟 등록 API <br/> 1. 사진 AWS S3 저장 <br/> 2. 태그 조회 3. 태그 등록 <br/> 4. 신규 태그 ID 조회 <br/> 5. 스팟 등록 <br/> 6. SpotHasTag 등록 <br/> 7. 포인트 획득 <br/>
     *
     * @param spotImage  : 스팟 사진 [NotNull]
     * @param tags       : 태그 정보
     * @param landmarkId : 랜드마크 정보 [Nullable]
     * @param memberId   : 현재 접근중인 주체
     * @return Long : 추가된 spotId
     */
    @Override
    public Long insertSpotAndTags(MultipartFile spotImage, List<TagRequestDto> tags, Long landmarkId, Long memberId) {
        // 1. 스팟 사진 S3에 저장
        SpotImage image = awsS3Service.uploadSpotImage(spotImage);
        log.debug("S3 저장 성공");

        // 2. 태그 조회
        List<Tag> existTags = tagQueryRepository.findAllByNames(
                tags.stream().map(TagRequestDto::getName).collect(Collectors.toList())
        );
        log.debug("-------------이미 등록된 태그 조회-------------");

        Set<String> existTagNames = existTags.stream().map(Tag::getName).collect(Collectors.toSet());
        List<Long> existTagIds = existTags.stream().map(Tag::getId).collect(Collectors.toList());
        log.debug("{} {}", existTagIds, existTagNames);

        // 3. 태그 등록
        List<TagRequestDto> newTags = tags.stream()
                                          .filter(tag -> !existTagNames.contains(tag.getName()))
                                          .collect(Collectors.toList());
        List<Long> newTagIds = new ArrayList<>();
        if (!newTags.isEmpty()) {
            tagJDBCRepository.bulkInsertTags(newTags);
            log.debug("---------------태그 등록-------------------");
            log.debug("{}", newTags);
            // 4. 신규 태그 조회
            newTagIds = tagQueryRepository.findAllByNames(newTags.stream().map(TagRequestDto::getName).collect(Collectors.toList()))
                                          .stream().map(Tag::getId).collect(Collectors.toList());
            log.debug("-------------------신규 태그 조회--------------");
            log.debug("{}", newTagIds);
        }

        // 5. 스팟 등록
        Long spotId = insertSpot(landmarkId, memberId, image);
        log.debug("----------------------스팟 등록-----------------");
        log.debug("{}", spotId);

        // 6. SpotHasTag 등록
        if (!tags.isEmpty()) {
            tagJDBCRepository.bulkInsertSpotHasTags(spotId,
                                                    Stream.concat(existTagIds.stream(), newTagIds.stream()).collect(Collectors.toList()));
            log.debug("-----------SpotHasTags 등록-----------------");
        }
        // 7. 포인트 획득
        memberService.gainPoint(memberId, PointMoney.SPOT_POINT.getValue());
        log.debug("--------------포인트 획득--------------------");
        return spotId;
    }

    private Long insertSpot(Long landmarkId, Long memberId, SpotImage image) {
        return spotRepository.save(SpotMapper.toSpot(
                image,
                landmarkId == null ? null : landmarkQueryRepository.getReferenceById(landmarkId),
                memberQueryRepository.getReferenceById(memberId)
        )).getId();
    }

    /**
     * 스팟 삭제
     *
     * @param spotId   : 스팟 id
     * @param memberId : 멤버 id
     * @throws ResourceConflictException : 이미 삭제된 리소스를 삭제하려고 하면 발생합니다. - Conflict
     * @throws ResourceNotFoundException : 존재하지 않는 리소스에 접근 시 발생합니다. - NotFound
     */
    @Override
    public void deleteSpot(Long spotId, Long memberId) throws ResourceConflictException, ResourceNotFoundException {
        Optional<Spot> spot = spotRepository.findByIdAndMemberId(spotId, memberId);
        spot.ifPresentOrElse(
                s -> {
                    if (Boolean.TRUE.equals(s.getDeleted())) {
                        throw new ResourceConflictException("이미 삭제된 리소스입니다.");
                    }
                    s.deleteSpot();
                },
                () -> {
                    throw new ResourceNotFoundException("해당 리소스 존재하지 않습니다.");
                }
        );
    }

}
