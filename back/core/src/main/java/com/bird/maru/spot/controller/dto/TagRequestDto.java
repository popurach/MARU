package com.bird.maru.spot.controller.dto;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TagRequestDto {

    @Nullable
    private Long id;
    @NotNull
    private String name;

}
