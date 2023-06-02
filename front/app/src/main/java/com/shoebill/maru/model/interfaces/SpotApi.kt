package com.shoebill.maru.model.interfaces

import com.shoebill.maru.model.data.Spot
import com.shoebill.maru.model.data.request.SpotClusterDTO
import com.shoebill.maru.model.data.spot.SaveSpot
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

    @GET("/api/spots/map")
    suspend fun getAroundSpots(
        @Query("west") west: Double,
        @Query("south") south: Double,
        @Query("east") east: Double,
        @Query("north") north: Double,
        @Query("filter") filter: String,
        @Query(
            "lastOffset",
            encoded = true
        ) lastOffset: Long? = null,
        @Query("size") size: Int = 20,
        @Query("tagId") tagId: Long?,
    ): Response<List<Spot>>

    @Multipart
    @POST("/api/spots") // spot 등록
    suspend fun saveSpot(
        @Part spotImage: MultipartBody.Part? = null,
        @Part("data") data: SaveSpot,
    ): Response<Long>


    @GET("api/spots/my")
    suspend fun getMySpots(
        @Query(
            "lastOffset",
            encoded = true
        ) lastOffset: Long? = 0, // encoded true 일 경우, null 일때는 포함 안됨
        @Query("size") size: Int = 20,
    ): Response<List<Spot>>

    @GET("api/spots/my/scraps")
    suspend fun getMyScrapedSpots(
        @Query(
            "lastOffset",
            encoded = true
        ) lastOffset: Long? = 0, // encoded true 일 경우, null 일때는 포함 안됨
        @Query("size") size: Int = 20,
    ): Response<List<Spot>>

    // 지도 기반 스팟 목록 조회
    @POST("/api/map/spots")
    suspend fun getSpotMarker(
        @Body parameter: SpotClusterDTO,
    ): Response<List<SpotMarker>>

    @GET("/api/spots/{id}")
    suspend fun getSpotDetail(
        @Path("id") id: Long,
    ): Response<Spot>

    @POST("/api/spots/{spotId}/like")
    suspend fun toggleLike(
        @Path("spotId") spotId: Long,
    ): Response<Unit>

    @POST("/api/spots/{spotId}/scrap")
    suspend fun toggleScrap(
        @Path("spotId") spotId: Long,
    ): Response<Unit>
}