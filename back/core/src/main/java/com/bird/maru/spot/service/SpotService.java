package com.bird.maru.spot.service;

import com.bird.maru.common.exception.ResourceConflictException;
import com.bird.maru.common.exception.ResourceNotFoundException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface SpotService {

    Long insertSpotAndTags(MultipartFile spotImage, List<String> tags, Long landmarkId, Long memberId);

    void deleteSpot(Long spotId, Long memberId) throws ResourceConflictException, ResourceNotFoundException;

}
