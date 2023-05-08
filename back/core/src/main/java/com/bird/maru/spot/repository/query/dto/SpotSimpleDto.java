package com.bird.maru.spot.repository.query.dto;

import com.bird.maru.domain.model.entity.Tag;
import java.net.URL;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
@ToString
@EqualsAndHashCode
public class SpotSimpleDto {

    private Long id;

    private Long landmarkId;

    private URL imageUrl;

    private Integer likeCount;

    private List<Tag> tags;

    @Builder.Default
    private Boolean liked = Boolean.FALSE;

    @Builder.Default
    private Boolean scraped = Boolean.FALSE;

    @Builder
    public SpotSimpleDto(Long id, Long landmarkId, URL imageUrl) {
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

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

}
