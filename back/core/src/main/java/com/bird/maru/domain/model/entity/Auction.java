package com.bird.maru.domain.model.entity;

import com.bird.maru.domain.model.type.id.AuctionId;
import java.time.LocalDate;
import javax.persistence.Column;
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
@Table(name = "auctions")
@IdClass(AuctionId.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
public class Auction {

    @Id
    @Column(name = "created_date", columnDefinition = "DATE", updatable = false)
    private LocalDate createdDate;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "landmark_id", updatable = false)
    private Landmark landmark;

    @NotNull
    @Builder.Default
    private Boolean finished = Boolean.FALSE;

    @Column(name = "last_log_id")
    private Long lastLogId;

}
