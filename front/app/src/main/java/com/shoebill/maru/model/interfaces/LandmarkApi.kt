package com.shoebill.maru.model.interfaces

import com.shoebill.maru.model.data.Landmark
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LandmarkApi {
    @GET("/api/map/landmarks")
    suspend fun getLandmarkByPos(
        @Query("west") west: Double,
        @Query("south") south: Double,
        @Query("east") east: Double,
        @Query("north") north: Double,
    ): Response<List<Landmark>>
}