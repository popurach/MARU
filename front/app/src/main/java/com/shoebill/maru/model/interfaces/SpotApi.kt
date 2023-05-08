package com.shoebill.maru.model.interfaces

import com.shoebill.maru.model.data.Spot
import com.shoebill.maru.model.data.request.SpotClusterDTO
import com.shoebill.maru.model.data.spot.SpotMarker
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface SpotApi {

    @Multipart
    @POST("/api/spots") // spot 등록
    suspend fun saveSpot(
        @Part spotImage: MultipartBody.Part? = null,
        @Part tags: MultipartBody.Part? = null,
        @Part landmarkId: MultipartBody.Part? = null,
    ): Long


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

    @GET("/api/spots/{id}")
    suspend fun getSpotDetail(
        @Path("id") id: Long
    ): Response<Spot>
}