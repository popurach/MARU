package com.shoebill.maru.model.interfaces

import com.shoebill.maru.model.data.Spot
import com.shoebill.maru.model.data.request.SpotClusterDTO
import com.shoebill.maru.model.data.spot.SpotMarker
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
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

    @POST("/api/map/spots")
    suspend fun getSpotMarker(
        @Body parameter: SpotClusterDTO
    ): List<SpotMarker>
}