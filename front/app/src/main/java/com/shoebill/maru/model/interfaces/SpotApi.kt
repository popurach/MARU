package com.shoebill.maru.model.interfaces

import com.shoebill.maru.model.data.Spot
import retrofit2.http.GET
import retrofit2.http.Query

interface SpotApi {

    @GET("api/spots/my")
    suspend fun getMySpots(
        @Query(
            "lastOffset",
            encoded = true
        ) lastOffset: Long? = 0, // encoded true 일 경우, null 일때는 포함 안됨
        @Query("size") size: Int = 20
    ): List<Spot>

    @GET("api/spots/my/scraps")
    suspend fun getMyScrapedSpots(
        @Query(
            "lastOffset",
            encoded = true
        ) lastOffset: Long? = 0, // encoded true 일 경우, null 일때는 포함 안됨
        @Query("size") size: Int = 20
    ): List<Spot>
}