package com.bird.maru.point.service;

public interface PointService {

    void landmarkOccupying(Long memberId);

    void landmarkPhoto(Long memberId);

    void spotMaking(Long memberId);

    void photoLike(Long memberId);

}
