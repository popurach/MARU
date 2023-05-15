package com.bird.maru.domain.model.document;

import com.bird.maru.domain.model.entity.Tag;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Setting;

@Document(indexName = "tags")
@Setting(settingPath = "/elastic/tags-settings.json")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class TagDoc {

    private Long id;

    private String name;

    public TagDoc(Tag tag) {
        this.id = tag.getId();
        this.name = tag.getName();
    }

}
