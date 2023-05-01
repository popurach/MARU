package com.bird.maru.spot.repository.query.dto;

import com.bird.maru.domain.model.entity.Tag;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
@ToString
@EqualsAndHashCode
public class SpotSimpleDto {

    private Long id;

    private Long landmarkId;

    private String imageUrl;

    private List<Tag> tags;

    private Boolean liked;

    private Boolean scraped;

}
