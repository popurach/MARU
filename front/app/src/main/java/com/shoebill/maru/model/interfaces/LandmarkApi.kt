package com.shoebill.maru.model.interfaces

import com.shoebill.maru.model.data.Stamp
import retrofit2.http.GET
import retrofit2.http.Query

interface LandmarkApi {
    @GET("/api/landmarks/my/stamps")
    suspend fun getVisitedLandmarks(
        @Query(
            "lastOffset",
            encoded = true
        ) lastOffset: Long?, // encoded true 일 경우, null 일때는 포함 안됨
        @Query("size") size: Int = 20
    ): List<Stamp>
}