package com.shoebill.maru.model.interfaces

import com.shoebill.maru.model.data.KakaoSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MyKakaoMapApi {
    @GET("v2/local/search/keyword.json")
    suspend fun getRecommendPlacesByKeyword(
        @Query("query") query: String,
        @Query("size") size: Int = 5
    ): Response<KakaoSearchResponse>
}