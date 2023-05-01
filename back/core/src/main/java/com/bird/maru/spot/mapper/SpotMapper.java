package com.bird.maru.spot.mapper;

import com.bird.maru.domain.model.entity.Spot;
import com.bird.maru.domain.model.entity.SpotHasTag;
import com.bird.maru.spot.repository.query.dto.SpotSimpleDto;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SpotMapper {

    default SpotSimpleDto toSpotSimpleDto(Spot spot) {
        return SpotSimpleDto.builder()
                            .id(spot.getId())
                            .landmarkId(spot.getLandmark() != null ? spot.getLandmark().getId() : null)
                            .imageUrl(spot.getImage().getUrl().toString())
                            .tags(
                                    spot.getTags()
                                        .stream()
                                        .map(SpotHasTag::getTag)
                                        .collect(Collectors.toList())
                            )
                            .build();
    }

    default List<SpotSimpleDto> toSpotSimpleDto(List<Spot> spot) {
        return spot.stream()
                   .map(this::toSpotSimpleDto)
                   .collect(Collectors.toList());
    }

}
