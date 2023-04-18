package com.bird.maru.domain.model.type;

import java.net.URL;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
@ToString
@EqualsAndHashCode
public class Image {

    @Column(name = "saved_path")
    @NotNull
    private String savedPath;

    @Column(name = "image_url")
    @NotNull
    private URL imageUrl;

}
