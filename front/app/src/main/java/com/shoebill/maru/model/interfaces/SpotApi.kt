package com.shoebill.maru.model.interfaces

import com.shoebill.maru.model.data.Spot
import retrofit2.http.GET
import retrofit2.http.Query

interface SpotApi {

    @GET("api/spots/my")
    suspend fun getMySpots(
        @Query("lastOffset") lastOffset: Long = 0,
        @Query("size") size: Int = 20
    ): List<Spot>
}