package com.bird.maru.domain.model.entity;

import com.bird.maru.domain.model.type.BaseDateTime;
import com.bird.maru.domain.model.type.Coordinate;
import com.bird.maru.domain.model.type.Image;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "spots",
        indexes = @Index(name = "spot_geo_index", columnList = "lng, lat")
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
public class Spot extends BaseDateTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @NotNull
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "landmark_id")
    private Landmark landmark;

    @Embedded
    @NotNull
    private Image image;

    @Embedded
    @NotNull
    private Coordinate coordinate; // (경도, 위도)

    @Column(name = "like_count")
    @NotNull
    @Builder.Default
    private Integer likeCount = 0;

    @NotNull
    @Builder.Default
    private Boolean deleted = Boolean.FALSE;

    @OneToMany(
            mappedBy = "spot",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private List<SpotHasTag> tags = new ArrayList<>();

    public boolean isLandmark() {
        return this.landmark != null;
    }

    public void addTag(Tag tag) {
        this.tags.add(
                SpotHasTag.builder()
                          .spot(this)
                          .tag(tag)
                          .build()
        );
    }

}
