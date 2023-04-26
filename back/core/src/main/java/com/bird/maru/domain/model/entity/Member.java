package com.bird.maru.domain.model.entity;

import com.bird.maru.domain.model.type.BaseDateTime;
import com.bird.maru.domain.model.type.Image;
import com.bird.maru.domain.model.type.Provider;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "members",
        uniqueConstraints = @UniqueConstraint(columnNames = { "email", "provider" })
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
public class Member extends BaseDateTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String nickname;

    @NotNull
    @Builder.Default
    private Integer point = 0;

    @Embedded
    @NotNull
    @Builder.Default
    private Image image = Image.getDefaultMemberProfile();

    @NotNull
    @Email
    private String email;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Provider provider;

    @NotNull
    @Builder.Default
    private Boolean deleted = Boolean.FALSE;

    @Column(name = "notice_token")
    private String noticeToken;

    // 유찰자 페이백 처리
    public void changePoint(int price) {
        this.point += price;
    }

    // 입찰자 입찰 처리
    public void bidPoint(int price) {
        this.point -= price;
    }
}
