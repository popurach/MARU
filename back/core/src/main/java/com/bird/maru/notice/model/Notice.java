package com.bird.maru.notice.model;

import java.time.LocalDateTime;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@Builder
@Document(collection = "notices")
@NoArgsConstructor
@AllArgsConstructor
public class Notice {

    private String id;

    @Indexed
    private Long memberId;

    private String noticeToken;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String content;

    @CreatedDate
    @Indexed(expireAfter = "30d") // 30일 뒤에 만료
    private LocalDateTime createdDateTime;

}
