package com.bird.maru.domain.model.type;


import com.fasterxml.jackson.annotation.JsonCreator;

public enum MapFilterType {
    ALL, MINE;

    @JsonCreator
    public static MapFilterType from(String s) {
        return MapFilterType.valueOf(s.trim().toUpperCase());
    }

}
