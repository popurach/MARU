package com.shoebill.maru.model.interfaces

import com.shoebill.maru.model.data.KakaoSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MyKakaoMapApi {
    @GET("v2/local/search/keyword.json")
    suspend fun getRecommendPlacesByKeyword(
        @Query("query") query: String,
        @Query("size") size: Int = 5,
        @Query("x") x: String = "126.9780", // 서울 중심 좌표
        @Query("y") y: String = "37.5665",
        @Query("radius") radius: Int = 18000 // 서울 반경 17키로
    ): Response<KakaoSearchResponse>
}