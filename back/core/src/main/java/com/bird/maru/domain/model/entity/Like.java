package com.bird.maru.domain.model.entity;

import com.bird.maru.domain.model.type.BaseDateTime;
import com.bird.maru.domain.model.type.id.LikeId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "likes")
@IdClass(LikeId.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
public class Like extends BaseDateTime {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", updatable = false)
    private Member member;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spot_id", updatable = false)
    private Spot spot;

    @NotNull
    @Builder.Default
    private Boolean deleted = Boolean.FALSE;

}
