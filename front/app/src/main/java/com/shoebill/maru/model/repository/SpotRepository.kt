package com.shoebill.maru.model.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.navigation.NavHostController
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.shoebill.maru.model.data.Spot
import com.shoebill.maru.model.data.Tag
import com.shoebill.maru.model.data.request.BoundingBox
import com.shoebill.maru.model.data.request.SpotClusterDTO
import com.shoebill.maru.model.data.spot.SaveSpot
import com.shoebill.maru.model.data.spot.SpotMarker
import com.shoebill.maru.model.interfaces.SpotApi
import com.shoebill.maru.model.source.SpotSource
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject


class SpotRepository @Inject constructor(
    private val spotApi: SpotApi
) {
    fun getAroundSpots(
        navController: NavHostController,
        west: Double,
        south: Double,
        east: Double,
        north: Double,
        filter: String = "ALL",
        tagId: Long?
    ): LiveData<PagingData<Spot>> = Pager(config = PagingConfig(pageSize = 20)) {
        SpotSource(spotApi, navController, west, south, east, north, filter, tagId)
    }.liveData

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

        val data = SaveSpot(tags = tagsList, landmarkId = landmarkId)

        return spotApi.saveSpot(
            spotImage = spotImageParam,
            data = data
        )
    }

    suspend fun getMySpots(lastOffset: Long?): List<Spot> =
        spotApi.getMySpots(lastOffset = lastOffset, size = 20)

    suspend fun getMyScrapedSpots(lastOffset: Long?): List<Spot> =
        spotApi.getMyScrapedSpots(lastOffset = lastOffset, size = 20)

    suspend fun getSpotMarker(
        boundingBox: BoundingBox,
        mine: Boolean,
        tagId: Long? = null
    ): Response<List<SpotMarker>> =
        spotApi.getSpotMarker(
            SpotClusterDTO(boundingBox, filter = if (mine) "mine" else "all", tagId)
        )

    suspend fun getSpotDetail(spotId: Long): Response<Spot> = spotApi.getSpotDetail(spotId)

    suspend fun toggleLike(spotId: Long) = spotApi.toggleLike(spotId)
    suspend fun toggleScrap(spotId: Long) = spotApi.toggleScrap(spotId)

}