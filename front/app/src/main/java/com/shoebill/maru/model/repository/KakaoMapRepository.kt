package com.shoebill.maru.model.repository

import com.shoebill.maru.model.data.KakaoSearchResponse
import com.shoebill.maru.model.interfaces.MyKakaoMapApi
import retrofit2.Response
import javax.inject.Inject

class KakaoMapRepository @Inject constructor(private val myKakaoMapApi: MyKakaoMapApi) {
    suspend fun getRecommendPlacesByKeyword(query: String): Response<KakaoSearchResponse> =
        myKakaoMapApi.getRecommendPlacesByKeyword(query = query)

}