package com.bird.maru.notice.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class Message {

    private Category category;

    private String content;

    public Message(Category category, String content) {
        this.category = category;
        this.content = content;
    }

}


