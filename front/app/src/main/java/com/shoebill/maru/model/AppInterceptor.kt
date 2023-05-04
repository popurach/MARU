package com.shoebill.maru.model

import com.shoebill.maru.util.PreferenceUtil
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AppInterceptor @Inject constructor(
    private val prefUtil: PreferenceUtil,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originRequest = chain.request()
        val accessToken = prefUtil.getString("accessToken", "")

        // 1. accessToken 존재한 다면 header 에 추가
        if (accessToken != "") {
            val requestWithAccessToken =
                originRequest.newBuilder().header("Authorization", "Bearer $accessToken")
                    .build()
            val response = chain.proceed(requestWithAccessToken)

            // 2. 요청 결과가 401 이라면 refresh token 으로 access token을 재발급 받음
            if (response.code == 401) {
                response.close()
                val refreshToken = prefUtil.getString("refreshToken", "")
                if (refreshToken != "") {
                    // 재발급 api 호출 -> 결과는 accessToken : String
                    val refreshRequest =
                        originRequest.newBuilder().header("Authorization", "Bearer $refreshToken")
                            .url("http://k8a403.p.ssafy.io/api/auth/access-token").build()
                    val refreshResponse = chain.proceed(refreshRequest)

                    // 3. 재발급 받은 accessToken 으로 동일 요청 시도
                    // 재발급 성공시
                    if (refreshResponse.isSuccessful) {
                        val newAccessToken = refreshResponse.headers["Access-Token"]
                        prefUtil.setString("accessToken", newAccessToken!!)

                        val requestWithNewAccessToken = originRequest.newBuilder()
                            .header("Authorization", "Bearer $newAccessToken")
                            .build()
                        val newResponse = chain.proceed(requestWithNewAccessToken)
                        // 4. 재발급 받은 accessToken 으로 시도했는데 401이면 재 로그인
                        if (newResponse.code == 401) {
                            // 재발급 받은 토큰으로 안되면 로그인 취소
                            prefUtil.clear()
                        }
                        return newResponse
                    } else {
                        // 재발급 실패시, 로그인 취소
                        prefUtil.clear()
                    }
                    return refreshResponse
                }
            }
            return response
        }
        return chain.proceed(originRequest)
    }
}