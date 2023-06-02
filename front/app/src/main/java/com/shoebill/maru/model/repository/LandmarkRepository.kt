package com.shoebill.maru.model.repository

import com.shoebill.maru.model.data.Stamp
import com.shoebill.maru.model.data.landmark.Landmark
import com.shoebill.maru.model.data.landmark.LandmarkName
import com.shoebill.maru.model.data.landmark.Owner
import com.shoebill.maru.model.data.landmark.SpotImage
import com.shoebill.maru.model.interfaces.LandmarkApi
import retrofit2.Response
import javax.inject.Inject

class LandmarkRepository @Inject constructor(
    private val landmarkApi: LandmarkApi,
) {
    suspend fun getVisitedLandmarks(lastOffset: Long?): Response<List<Stamp>> =
        landmarkApi.getVisitedLandmarks(lastOffset = lastOffset)

    suspend fun getLandmarkByPos(
        west: Double,
        south: Double,
        east: Double,
        north: Double,
    ): Response<List<Landmark>> = landmarkApi.getLandmarkByPos(west, south, east, north)

    suspend fun getLandmarkOwner(landmarkId: Long): Response<Owner> =
        landmarkApi.getLandmarkOwner(landmarkId)

    suspend fun getLandmarkName(landmarkId: Long): Response<LandmarkName> =
        landmarkApi.getLandmarkName(landmarkId)

    suspend fun getLandmarkImages(lastOffset: Long?, landmarkId: Long): Response<List<SpotImage>> =
        landmarkApi.getImageUrls(lastOffset = lastOffset, id = landmarkId)

    suspend fun visitLandmark(landmarkId: Long): Response<Int> =
        landmarkApi.visitLandmark(landmarkId)
}