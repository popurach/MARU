package com.bird.maru.spot.controller.dto;

import com.bird.maru.domain.model.entity.Tag;
import java.net.URL;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class SpotDetailResponseDto {

    private Long id;
    private Long landmarkId;
    private URL imageUrl;
    private Boolean scraped;
    private Boolean liked;
    private Integer likeCount;
    private List<Tag> tags;

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

}
