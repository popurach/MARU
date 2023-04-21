package com.bird.maru.domain.model.entity;

import com.bird.maru.domain.model.type.BaseTime;
import com.bird.maru.domain.model.type.Image;
import com.bird.maru.domain.model.type.Provider;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
public class Member extends BaseTime {

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
    private Provider provider;

    @NotNull
    @Builder.Default
    private Boolean deleted = Boolean.FALSE;

    private String alarmToken;

}
