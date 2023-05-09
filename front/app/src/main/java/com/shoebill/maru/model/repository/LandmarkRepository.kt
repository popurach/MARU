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
    private val TAG = "LANDMARK"
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
            Log.d("LANDMARK-OWNER", "${response.body()}")
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

    suspend fun visitLandmark(landmarkId: Long): Int {
        val response = landmarkApi.visitLandmark(landmarkId)
        Log.d(TAG, "${response.isSuccessful}")
        if (response.isSuccessful) {
            return response.body()!!
        }
        Log.d(TAG, "visit landmark fail: ${response.code()}")
        return -1
    }

}