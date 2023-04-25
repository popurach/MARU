package com.bird.maru.domain.model.entity;

import com.bird.maru.domain.model.type.Coordinate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "landmarks",
        indexes = @Index(name = "landmark_geo_index", columnList = "lng, lat")
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
public class Landmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id")
    private Long memberId; // 현재 랜드마크의 대표 회원

    private String name;

    @Embedded
    @NotNull
    private Coordinate coordinate; // (경도, 위도)

    @Column(name = "visit_count")
    @NotNull
    @Builder.Default
    private Integer visitCount = 0;

}
