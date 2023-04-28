package com.bird.maru.landmark.controller;

import com.bird.maru.common.exception.ResourceNotFoundException;
import com.bird.maru.landmark.controller.dto.LandmarkResponseDto;
import com.bird.maru.landmark.mapper.LandmarkMapper;
import com.bird.maru.landmark.service.query.LandmarkQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/landmarks")
public class LandmarkController {

    private final LandmarkQueryService landmarkQueryService;

    /**
     * 랜드마크 정보 조회 - 이름 반환
     *
     * @param id : pathVariable 로 landmark id 전달
     * @return LandmarkResponseDto
     * @throws ResourceNotFoundException : DB에 해당 리소스 존재하지 않음
     */
    @GetMapping("/{id}")
    public LandmarkResponseDto findLandmark(@PathVariable Long id) throws ResourceNotFoundException {
        return LandmarkMapper.toLandmarkResponseDto(landmarkQueryService.findLandmark(id));
    }

}
