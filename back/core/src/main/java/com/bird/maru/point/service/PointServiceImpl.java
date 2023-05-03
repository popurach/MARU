package com.bird.maru.point.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PointServiceImpl implements  PointService{

    @Override
    public void landmarkOccupying(Long memberId) {

    }

    @Override
    public void landmarkPhoto(Long memberId) {

    }

    @Override
    public void spotMaking(Long memberId) {

    }

    @Override
    public void photoLike(Long memberId) {

    }

}
