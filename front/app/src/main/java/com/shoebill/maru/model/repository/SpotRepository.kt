package com.shoebill.maru.model.repository

import com.shoebill.maru.model.data.Spot
import com.shoebill.maru.model.data.request.BoundingBox
import com.shoebill.maru.model.data.request.SpotClusterDTO
import com.shoebill.maru.model.data.spot.SpotMarker
import com.shoebill.maru.model.interfaces.SpotApi
import javax.inject.Inject

class SpotRepository @Inject constructor(
    private val spotApi: SpotApi
) {
    suspend fun saveSpot(): Long {
        return spotApi.saveSpot()
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
}