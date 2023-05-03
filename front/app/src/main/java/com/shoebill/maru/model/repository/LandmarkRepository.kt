package com.shoebill.maru.model.repository

import com.shoebill.maru.model.data.Stamp
import com.shoebill.maru.model.interfaces.LandmarkApi
import javax.inject.Inject

class LandmarkRepository @Inject constructor(
    private val landmarkApi: LandmarkApi
) {

    suspend fun getVisitedLandmarks(lastOffset: Long?): List<Stamp> =
        landmarkApi.getVisitedLandmarks(lastOffset = lastOffset)
}