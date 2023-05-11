package com.shoebill.maru.model

import com.shoebill.maru.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class KakaoInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originRequest = chain.request()
        val key = BuildConfig.KAKAO_REST_API_KEY
        val authenticatedRequest =
            originRequest.newBuilder().header("Authorization", "KakaoAK $key")
                .build()
        
        return chain.proceed(authenticatedRequest)
    }
}