package com.bird.maru.spot.controller.dto;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
public class SpotPostRequestDto {

    @NotNull
    private MultipartFile spotImage;
    @Size(max = 5, message = "태그는 5개 이하로 입력해주세요.")
    private final List<TagRequestDto> tags = new ArrayList<>();
    @Nullable
    private Long landmarkId;

}
