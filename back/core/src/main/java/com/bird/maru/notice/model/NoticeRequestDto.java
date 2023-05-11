package com.bird.maru.notice.model;

import com.bird.maru.domain.model.entity.Landmark;
import com.bird.maru.domain.model.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeRequestDto {

    private Member member;

    private Landmark landmark;

    private Integer point;

}
