package com.shoebill.maru.model.repository

import android.util.Log
import com.shoebill.maru.model.data.Stamp
import com.shoebill.maru.model.data.landmark.Landmark
import com.shoebill.maru.model.data.landmark.Owner
import com.shoebill.maru.model.data.landmark.SpotImage
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

    suspend fun getLandmarkOwner(landmarkId: Long): Owner {
        val response = landmarkApi.getLandmarkOwner(landmarkId)
        if (response.isSuccessful) {
            return response.body() ?: Owner()
        }
        Log.d("LANDMARK", "getLandmarkOwner fail ${response.errorBody()}")
        return Owner()
    }

    suspend fun getLandmarkName(landmarkId: Long): String {
        val response = landmarkApi.getLandmarkName(landmarkId)
        if (response.isSuccessful) {
            return response.body()?.name ?: ""
        }
        return ""
    }

    suspend fun getLandmarkImages(lastOffset: Long?, landmarkId: Long): List<SpotImage> =
        landmarkApi.getImageUrls(lastOffset = lastOffset, id = landmarkId)

}