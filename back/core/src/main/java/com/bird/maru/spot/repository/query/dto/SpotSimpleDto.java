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

    private Integer likeCount;

    private List<Tag> tags;

    @Builder.Default
    private Boolean liked = Boolean.FALSE;

    @Builder.Default
    private Boolean scraped = Boolean.FALSE;

    @Builder
    public SpotSimpleDto(Long id, Long landmarkId, String imageUrl) {
        this.id = id;
        this.landmarkId = landmarkId;
        this.imageUrl = imageUrl;
    }

    public void checkLiked() {
        this.liked = Boolean.TRUE;
    }

    public void checkScraped() {
        this.scraped = Boolean.TRUE;
    }

}
