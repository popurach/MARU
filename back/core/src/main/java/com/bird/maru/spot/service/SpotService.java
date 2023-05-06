package com.bird.maru.spot.service;

import com.bird.maru.spot.controller.dto.TagRequestDto;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface SpotService {

    Long insertSpotAndTags(MultipartFile spotImage, List<TagRequestDto> tags, Long landmarkId, Long memberId);

    void deleteSpot(Long spotId, Long memberId);

}
