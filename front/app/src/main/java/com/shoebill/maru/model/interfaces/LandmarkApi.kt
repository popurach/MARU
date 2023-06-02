package com.shoebill.maru.model.interfaces

import com.shoebill.maru.model.data.Stamp
import com.shoebill.maru.model.data.landmark.Landmark
import com.shoebill.maru.model.data.landmark.LandmarkName
import com.shoebill.maru.model.data.landmark.Owner
import com.shoebill.maru.model.data.landmark.SpotImage
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface LandmarkApi {
    @GET("/api/landmarks/my/stamps")
    suspend fun getVisitedLandmarks(
        @Query(
            "lastOffset",
            encoded = true
        ) lastOffset: Long?, // encoded true 일 경우, null 일때는 포함 안됨
        @Query("size") size: Int = 20,
    ): Response<List<Stamp>>

    @GET("/api/map/landmarks")
    suspend fun getLandmarkByPos(
        @Query("west") west: Double,
        @Query("south") south: Double,
        @Query("east") east: Double,
        @Query("north") north: Double,
    ): Response<List<Landmark>>

    @GET("/api/landmarks/{id}/owner")
    suspend fun getLandmarkOwner(@Path("id") id: Long): Response<Owner>

    @GET("/api/landmarks/{id}")
    suspend fun getLandmarkName(@Path("id") id: Long): Response<LandmarkName>

    @GET("/api/landmarks/{id}/spots")
    suspend fun getImageUrls(
        @Path("id") id: Long,
        @Query(
            "lastOffset",
            encoded = true
        ) lastOffset: Long? = 0, // encoded true 일 경우, null 일때는 포함 안됨
        @Query("size") size: Int = 20,
    ): Response<List<SpotImage>>

    @POST("/api/landmarks/{id}")
    suspend fun visitLandmark(
        @Path("id") id: Long,
    ): Response<Int>

}