package com.shoebill.maru.model.repository

import com.shoebill.maru.model.data.Stamp
import com.shoebill.maru.model.data.Landmark
import com.shoebill.maru.model.interfaces.LandmarkApi
import javax.inject.Inject

class LandmarkRepository @Inject constructor(
    private val landmarkApi: LandmarkApi
) {

    suspend fun getVisitedLandmarks(lastOffset: Long?): List<Stamp> =
        landmarkApi.getVisitedLandmarks(lastOffset = lastOffset)

    suspend fun getLandmarkByPos(
        west: Double,
        south: Double,
        east: Double,
        north: Double
    ): List<Landmark>? {
        val response = landmarkApi.getLandmarkByPos(west, south, east, north)
        if (response.isSuccessful) {
            return response.body() ?: listOf()
        }
        return null
    }
}