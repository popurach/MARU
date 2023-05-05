package com.shoebill.maru.model.repository

import com.shoebill.maru.model.data.Spot
import com.shoebill.maru.model.interfaces.SpotApi
import javax.inject.Inject

class SpotRepository @Inject constructor(
    private val spotApi: SpotApi
) {
    suspend fun getMySpots(lastOffset: Long?): List<Spot> =
        spotApi.getMySpots(lastOffset = lastOffset, size = 20)

    suspend fun getMyScrapedSpots(lastOffset: Long?): List<Spot> =
        spotApi.getMyScrapedSpots(lastOffset = lastOffset, size = 20)
}