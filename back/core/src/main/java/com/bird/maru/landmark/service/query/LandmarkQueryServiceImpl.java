package com.bird.maru.landmark.service.query;

import com.bird.maru.common.exception.ResourceNotFoundException;
import com.bird.maru.domain.model.entity.Landmark;
import com.bird.maru.landmark.controller.dto.LandmarkMapResponseDto;
import com.bird.maru.landmark.controller.dto.LandmarkStampResponseDto;
import com.bird.maru.landmark.mapper.LandmarkMapper;
import com.bird.maru.landmark.repository.query.LandmarkCustomQueryRepository;
import com.bird.maru.landmark.repository.query.LandmarkQueryRepository;
import com.bird.maru.member.repository.query.MemberRedisRepository;
import com.bird.maru.spot.repository.query.SpotCustomQueryRepository;
import com.bird.maru.spot.repository.query.dto.SpotSimpleDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LandmarkQueryServiceImpl implements LandmarkQueryService {

    private final LandmarkQueryRepository landmarkQueryRepository;
    private final LandmarkCustomQueryRepository landmarkCustomQueryRepository;
    private final MemberRedisRepository memberRedisRepository;
    private final SpotCustomQueryRepository spotCustomQueryRepository;

    /**
     * 랜드마크 조회
     *
     * @param id : landmark's id
     * @return Landmark Entity
     * @throws ResourceNotFoundException : DB에 해당하는 리소스 없음
     */
    @Override
    public Landmark findLandmark(Long id) throws ResourceNotFoundException {
        return landmarkQueryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("해당 리소스 존재하지 않습니다.")
        );
    }

    /**
     * 랜드마크 지도 기반 조회 <br/> 매일마다 갱신되는 방문 여부 체크 로직 추가 Mapper를 통해 방문 여부를 처리한 List<ResponseDTO> 형태로 반환합니다.
     *
     * @param west     : minLng
     * @param south    : minLat
     * @param east     : maxLng
     * @param north    : maxLat
     * @param memberId : Long
     * @return List<LandmarkMapResponseDto>
     */
    @Override
    public List<LandmarkMapResponseDto> findLandmarkBasedMap(Double west, Double south, Double east, Double north, Long memberId) {
        List<Landmark> landmarks = landmarkCustomQueryRepository.findLandmarksBasedMap(west, south, east, north);
        if (landmarks.isEmpty()) {
            return new ArrayList<>();
        }
        Set<Long> visitedLandmarks = memberRedisRepository.findVisitedLandmarks(memberId);
        return LandmarkMapper.toLandmarkMapResponseDtos(landmarks, visitedLandmarks);
    }

    /**
     * 모든 랜드마크 with 나의 방문 정보 조회
     *
     * @param memberId : 현재 접근중인 주체
     * @return LandmarkStampResponseDto : 랜드마크 id, 스팟 id, 최신 스팟 사진, 랜드마크 이름
     */
    @Override
    public List<LandmarkStampResponseDto> findLandmarkStamps(Long memberId, Long lastOffset, Integer size) {
        List<Landmark> landmarks = landmarkCustomQueryRepository.findAll(lastOffset, size);
        Map<Long, SpotSimpleDto> spotSimpleMap = spotCustomQueryRepository.findSpotByMyVisitedLandmark(memberId,
                                                                                                       landmarks.stream().map(Landmark::getId)
                                                                                                                .collect(Collectors.toList()))
                                                                          .stream()
                                                                          .collect(Collectors.toMap(SpotSimpleDto::getLandmarkId,
                                                                                                    Function.identity()));
        return LandmarkMapper.toLandmarkStampResponseDtos(landmarks, spotSimpleMap);
    }

}
