package com.shoebill.maru.model.repository

import android.util.Log
import com.google.gson.Gson
import com.shoebill.maru.model.data.Spot
import com.shoebill.maru.model.data.Tag
import com.shoebill.maru.model.data.request.BoundingBox
import com.shoebill.maru.model.data.request.SpotClusterDTO
import com.shoebill.maru.model.data.spot.SpotMarker
import com.shoebill.maru.model.interfaces.SpotApi
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject


class SpotRepository @Inject constructor(
    private val spotApi: SpotApi
) {
    suspend fun getAroundSpots(
        west: Double,
        south: Double,
        east: Double,
        north: Double,
        filter: String = "ALL"
    ): List<Spot> =
        spotApi.getAroundSpots(west, south, east, north, filter)

    suspend fun saveSpot(spotImage: File, tags: List<Tag>?, landmarkId: Long?): Response<Long> {
        val spotImageParam = MultipartBody.Part.createFormData(
            name = "spotImage",
            filename = spotImage.name,
            body = spotImage.asRequestBody()
        )

        Log.d("SPOT", "saveSpot: ${spotImage.name}")

        Log.d("SPOT", "${tags!!.size}")
        val tagsList = ArrayList<String>()
        for (tag in tags) {
            tagsList.add(tag.name)
        }

        val tagsParam: RequestBody = Gson().toJson(tagsList)
            .toRequestBody("application/json".toMediaTypeOrNull())


        val landmarkIdParam = if (landmarkId != null) MultipartBody.Part.createFormData(
            name = "landmarkId",
            value = landmarkId.toString()
        ) else null

        return spotApi.saveSpot(
            spotImage = spotImageParam,
            tags = tagsParam,
            landmarkId = landmarkIdParam
        )
    }

    suspend fun getMySpots(lastOffset: Long?): List<Spot> =
        spotApi.getMySpots(lastOffset = lastOffset, size = 20)

    suspend fun getMyScrapedSpots(lastOffset: Long?): List<Spot> =
        spotApi.getMyScrapedSpots(lastOffset = lastOffset, size = 20)

    suspend fun getSpotMarker(boundingBox: BoundingBox, mine: Boolean): List<SpotMarker> =
        spotApi.getSpotMarker(
            SpotClusterDTO(boundingBox, mine)
        )

    suspend fun getSpotDetail(spotId: Long): Spot {
        val response = spotApi.getSpotDetail(spotId)
        if (response.isSuccessful) {
            return response.body()!!
        }
        throw Error("${response.code()}")
    }

    suspend fun toggleLike(spotId: Long) = spotApi.toggleLike(spotId)
    suspend fun toggleScrap(spotId: Long) = spotApi.toggleScrap(spotId)

}