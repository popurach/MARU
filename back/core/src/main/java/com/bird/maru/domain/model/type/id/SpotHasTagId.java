package com.bird.maru.domain.model.type.id;

import java.io.Serializable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class SpotHasTagId implements Serializable {

    private Long spot;

    private Long tag;

}
