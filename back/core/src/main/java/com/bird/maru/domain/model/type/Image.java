package com.bird.maru.domain.model.type;

import com.bird.maru.domain.converter.UrlConverter;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Converter;
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
    @Convert(converter = UrlConverter.class)
    private URL imageUrl;

    public static Image getDefaultMemberProfile() {
        try {
            return Image.builder()
                        .savedPath("images/members/default_profile.png")
                        .imageUrl(new URL("https://maruofbucket.s3.ap-northeast-2.amazonaws.com/images/members/default_profile.png"))
                        .build();
        } catch (MalformedURLException e) {
            throw new UncheckedIOException(e);
        }
    }

}
