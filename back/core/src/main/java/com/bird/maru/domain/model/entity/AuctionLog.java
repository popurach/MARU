package com.bird.maru.domain.model.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "auction_logs",
        indexes = @Index(name = "log_date_index", columnList = "created_date_time")
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
public class AuctionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "auction_created_date", referencedColumnName = "created_date", updatable = false),
            @JoinColumn(name = "landmark_id", referencedColumnName = "landmark_id", updatable = false)
    })
    private Auction auction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", updatable = false)
    private Member member;

    @NotNull
    private Integer price;

    @Column(name = "created_date_time", columnDefinition = "DATETIME")
    @NotNull
    private LocalDateTime createdDateTime;

    public AuctionLog(AuctionLog auctionLogs) {
        this.id = auctionLogs.getId();
        this.auction = auctionLogs.getAuction();
        this.member = auctionLogs.getMember();
        this.price = auctionLogs.getPrice();
        this.createdDateTime = auctionLogs.getCreatedDateTime();
    }

}
