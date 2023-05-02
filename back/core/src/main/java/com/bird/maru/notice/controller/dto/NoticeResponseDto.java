package com.bird.maru.notice.controller.dto;

import com.bird.maru.notice.model.Category;
import com.bird.maru.notice.model.Notice;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NoticeResponseDto {

    private Category category;
    private String content;
    private LocalDateTime createdDateTime;

    public NoticeResponseDto(Notice notice) {
        this.category = notice.getCategory();
        this.content = notice.getContent();
        this.createdDateTime = notice.getCreatedDateTime();
    }

}
