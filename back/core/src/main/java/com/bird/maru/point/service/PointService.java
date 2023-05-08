package com.bird.maru.point.service;

public interface PointService {

    Integer landmarkVisiting(Long memberId);

    Integer landmarkOccupying(Long memberId);

    Integer landmarkPhoto(Long memberId);

    Integer spotMaking(Long memberId);

    Integer photoLike(Long memberId);

}
