package com.bird.maru.notice.model;

import com.bird.maru.domain.model.entity.Member;
import lombok.Data;

@Data
public class NoticeRequestDto {

    private Member member;
    private Message message;

}
