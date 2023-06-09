package com.bird.maru.spot.mapper;

import com.bird.maru.cluster.geo.Marker;
import com.bird.maru.domain.model.entity.Landmark;
import com.bird.maru.domain.model.entity.Member;
import com.bird.maru.domain.model.entity.Spot;
import com.bird.maru.domain.model.entity.SpotHasTag;
import com.bird.maru.domain.model.entity.Tag;
import com.bird.maru.domain.model.type.Coordinate;
import com.bird.maru.landmark.controller.dto.LandmarkSpotResponseDto;
import com.bird.maru.spot.repository.query.dto.SpotSimpleDto;
import com.bird.maru.spot.service.dto.SpotImage;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SpotMapper {

    public static SpotSimpleDto toSpotSimpleDto(Spot spot) {
        return SpotSimpleDto.builder()
                            .id(spot.getId())
                            .landmarkId(toLandmarkId(spot))
                            .imageUrl(spot.getImage().getUrl())
                            .likeCount(spot.getLikeCount())
                            .tags(
                                    spot.getTags()
                                        .stream()
                                        .map(SpotHasTag::getTag)
                                        .collect(Collectors.toList())
                            )
                            .scraped(Boolean.FALSE)
                            .build();
    }

    public static List<SpotSimpleDto> toSpotSimpleDto(List<Spot> spots) {
        return spots.stream()
                    .map(SpotMapper::toSpotSimpleDto)
                    .collect(Collectors.toList());
    }

    public static SpotSimpleDto toSpotSimpleDto(Spot spot, Map<Long, List<Tag>> tagMap) {
        return SpotSimpleDto.builder()
                            .id(spot.getId())
                            .landmarkId(toLandmarkId(spot))
                            .imageUrl(spot.getImage().getUrl())
                            .likeCount(spot.getLikeCount())
                            .tags(tagMap.get(spot.getId()))
                            .scraped(Boolean.TRUE)
                            .build();
    }

    public static List<SpotSimpleDto> toSpotSimpleDto(List<Spot> spots, Map<Long, List<Tag>> tagMap) {
        return spots.stream()
                    .map(spot -> toSpotSimpleDto(spot, tagMap))
                    .collect(Collectors.toList());
    }

    private static Long toLandmarkId(Spot spot) {
        if (!spot.isLandmark()) {
            return null;
        }

        return spot.getLandmark().getId();
    }

    public static LandmarkSpotResponseDto toLandmarkSpotResponseDto(Spot spot) {
        return LandmarkSpotResponseDto.builder()
                                      .id(spot.getId())
                                      .imageUrl(spot.getImage().getUrl().toString())
                                      .build();
    }

    public static List<LandmarkSpotResponseDto> toLandmarkSpotResponseDtos(List<Spot> spots) {
        return spots.stream()
                    .map(SpotMapper::toLandmarkSpotResponseDto)
                    .collect(Collectors.toList());
    }

    public static Spot toSpot(SpotImage image, Landmark landmark, Member member) {
        return Spot.builder()
                   .landmark(landmark)
                   .member(member)
                   .image(image.getImage())
                   .coordinate(Coordinate.builder()
                                         .lng(image.getCoordinate().getLng())
                                         .lat(image.getCoordinate().getLat())
                                         .build())
                   .build();
    }

    public static Marker toMarker(Spot spot) {
        return Marker.builder()
                     .id(spot.getId())
                     .coordinate(spot.getCoordinate())
                     .build();
    }

    public static List<Marker> toMarkers(List<Spot> spots) {
        return spots.stream().map(SpotMapper::toMarker).collect(Collectors.toList());
    }

}
